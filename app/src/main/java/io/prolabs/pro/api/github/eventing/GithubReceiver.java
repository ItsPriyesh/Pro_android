package io.prolabs.pro.api.github.eventing;

import com.google.gson.JsonElement;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.utils.GitHubUtils;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import timber.log.Timber;

/**
 * Created by Edmund on 2015-03-07.
 */
public class GitHubReceiver {
    private static final int MAX_RETRIES = 5;
    private static GitHubReceiver instance = null;
    private static GitHubService service = GitHubApi.getService();
    private final Bus RECEIVE = new Bus(ThreadEnforcer.MAIN);
    private final Bus SEND = new Bus(ThreadEnforcer.ANY);
    private ConcurrentHashMap<Repo, Integer> retries = new ConcurrentHashMap<>();

    private GitHubReceiver() {
        SEND.register(this);
    }

    public static GitHubReceiver getInstance() {
        if (instance == null) {
            service = GitHubApi.getService();
            instance = new GitHubReceiver();
        }
        return instance;
    }

    public void register(Object obj) {
        RECEIVE.register(obj);
    }

    public void ask(Object obj) {
        SEND.post(obj);
    }

    @Subscribe
    public void requestLanguageForRepo(LanguageDataRequest request) {
        GitHubUser user = request.getUser();
        Repo repo = request.getRepo();
        service.getLanguages(user.getUsername(), repo.getName(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                List<Language> languages = GitHubUtils.parseLanguageResponse(jsonElement);
                RECEIVE.post(new LanguagesReceived(user, languages));
                for (Language language : languages)
                    Timber.i("Language received: " + language.getName() + " : " + language.getBytes());
            }

            @Override
            public void failure(RetrofitError error) {
                recoverFromRepoError(user, repo, error);
            }
        });
    }

    private boolean isRetryable(RetrofitError.Kind errorKind) {
        boolean retryable = false;
        switch (errorKind) {
            case NETWORK:
            case CONVERSION:
                retryable = true;
            case HTTP:
            case UNEXPECTED:
            default:
                retryable = false;
        }
        return retryable;
    }

    private boolean is404(RetrofitError error) {
        return error.getKind() == RetrofitError.Kind.HTTP &&
                error.getResponse().getStatus() == 404;
    }

    private void recoverFromRepoError(GitHubUser user, Repo repo, RetrofitError error) {
        RetrofitError.Kind errorKind = error.getKind();
        if (!is404(error)) {
            if (isRetryable(errorKind)) {
                tryToRetry(user, repo);
            } else {
                Timber.i("Failed to get repo: " + repo.getName());
            }
        }
    }

    private void tryToRetry(GitHubUser user, Repo repo) {
        Integer unsafeRetriesSoFar = retries.get(repo);
        int retriesSoFar = (unsafeRetriesSoFar == null) ? 0 : unsafeRetriesSoFar;
        if (retriesSoFar != MAX_RETRIES) {
            retries.put(repo, retriesSoFar + 1);
            SEND.post(new LanguageDataRequest(user, repo));
        }
    }
}
