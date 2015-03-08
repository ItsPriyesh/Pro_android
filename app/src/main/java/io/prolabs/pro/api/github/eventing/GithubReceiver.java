package io.prolabs.pro.api.github.eventing;

import com.google.gson.JsonElement;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.Gist;
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
    private static GitHubReceiver instance = null;
    private static GitHubService service = GitHubApi.getService();
    private final Bus RECEIVE = new Bus(ThreadEnforcer.MAIN);

    private GitHubReceiver() {
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

    public void requestAllLanguages() {
        RECEIVE.post(new ResetDataRequest());
        service.getRepos(GitHubApi.MAX_REPOS_PER_PAGE, new Callback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                for (Repo repo : repos) {
                    requestLanguageForRepo(repo);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public void getUser() {
        service.getAuthUser(new Callback<GitHubUser>() {
            @Override
            public void success(GitHubUser gitHubUser, Response response) {
                if (gitHubUser != null)
                    GitHubApi.setCurrentUser(gitHubUser);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void requestLanguageForRepo(final Repo repo) {
        GitHubUser user = GitHubApi.getCurrentUser();
        if (user == null) {
            getUser();
            return;
        }
        service.getLanguages(user.getUsername(), repo.getName(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                List<Language> languages = GitHubUtils.parseLanguageResponse(jsonElement);
                RECEIVE.post(new LanguagesReceived(repo, languages));
                for (Language language : languages)
                    Timber.i("Language received: " + language.getName() + " : " + language.getBytes());
            }

            @Override
            public void failure(RetrofitError error) {
                logRepoError(user, repo, error);
            }
        });
    }

    public void requestGists() {
        service.getGists(new Callback<List<Gist>>() {

            @Override
            public void success(List<Gist> gists, Response response) {
                RECEIVE.post(new GistsReceived(gists));
            }

            @Override
            public void failure(RetrofitError error) {
                logError(error);
            }
        });
    }

    private boolean is404(RetrofitError error) {
        return error.getKind() == RetrofitError.Kind.HTTP &&
                error.getResponse().getStatus() == 404;
    }

    private void logError(RetrofitError error) {
        Timber.i(error.getMessage());
    }

    private void logRepoError(GitHubUser user, Repo repo, RetrofitError error) {
        if (!is404(error))
            Timber.i("Failed to get repo: " + repo.getName());
        else
            Timber.i("Private repo could not be downloaded: " + repo.getName());
    }
}
