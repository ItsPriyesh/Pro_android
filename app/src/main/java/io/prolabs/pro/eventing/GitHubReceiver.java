package io.prolabs.pro.eventing;

import com.google.gson.JsonElement;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashSet;
import java.util.List;

import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.CommitActivity;
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


    private void requestCodeWeeksForRepo(final Repo repo) {
        GitHubUser currentUser = GitHubApi.getCurrentUser();
        service.getCodeFrequency(currentUser.getUsername(), repo.getName(), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                List<CodeWeek> codeWeeks = GitHubUtils.parseCodeFrequencyResponse(jsonElement);
                RECEIVE.post(new CodeWeeksReceived(repo, codeWeeks));
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.i("Failed to retrieve codeweeks for repo: " + repo.getName()+ ": " + error.getMessage());
            }
        });
    }

    public void requestAllStats() {
        service.getRepos(GitHubApi.MAX_REPOS_PER_PAGE, new Callback<List<Repo>>() {
            @Override
            public void success(List<Repo> repos, Response response) {
                RECEIVE.post(new ReposReceived(new HashSet<>(repos)));
                for (Repo repo : repos) {
                    requestLanguageForRepo(repo);
                    requestCodeWeeksForRepo(repo);
                    requestCommitHistoryForRepo(repo);
                }
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    private void requestCommitHistoryForRepo(Repo repo) {
        GitHubUser user = GitHubApi.getCurrentUser();
        service.getCommitActivity(user.getUsername(), repo.getName(), new Callback<List<CommitActivity>>() {
            @Override
            public void success(List<CommitActivity> commitActivity, Response response) {
                RECEIVE.post(new CommitsReceived(repo, commitActivity));
            }

            @Override
            public void failure(RetrofitError error) {
                Timber.i("Failed to retrieve commit history for " + repo.getName() + ": " + error.getMessage());
            }
        });
    }


    private void requestLanguageForRepo(final Repo repo) {
        GitHubUser user = GitHubApi.getCurrentUser();
        if (user == null) {
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
                logRepoError(repo, error);
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

    private void logRepoError(Repo repo, RetrofitError error) {
        if (!is404(error))
            Timber.i("Failed to get repo: " + repo.getName());
        else
            Timber.i("Private repo could not be downloaded: " + repo.getName());
    }
}
