package io.prolabs.pro.eventing;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import java.util.HashSet;
import java.util.List;

import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.GitHubUser;
import io.prolabs.pro.models.github.Language;
import io.prolabs.pro.models.github.Repo;
import io.prolabs.pro.utils.GitHubUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static io.prolabs.pro.utils.CallbackUtils.callback;

/**
 * Created by Edmund on 2015-03-07.
 */
public class GitHubRequester {
    private static GitHubRequester instance = new GitHubRequester();
    private final Bus bus = new Bus(ThreadEnforcer.MAIN);

    private GitHubRequester() {
    }

    public static GitHubRequester getInstance() {
        return instance;
    }

    void register(Object obj) {
        bus.register(obj);
    }

    private void requestCodeWeeksForRepo(final Repo repo) {
        GitHubUser currentUser = GitHubApi.getCurrentUser();
        GitHubApi.getService().getCodeFrequency(currentUser.getUsername(), repo.getName(), callback(
                (jsonElement) -> {
                    List<CodeWeek> codeWeeks = GitHubUtils.parseCodeFrequencyResponse(jsonElement);
                    bus.post(new CodeWeeksReceived(repo, codeWeeks));
                },
                error -> Timber.i("Failed to retrieve codeweeks for repo: " + repo.getName() + ": " + error.getMessage())
        ));
    }

    public void requestAllStats() {
        requestGists();
        GitHubApi.getService().getRepos(GitHubApi.MAX_REPOS_PER_PAGE, callback(
                        repos -> {
                            bus.post(new ReposReceived(new HashSet<>(repos)));
                            for (Repo repo : repos) {
                                requestLanguageForRepo(repo);
                                requestCodeWeeksForRepo(repo);
                                requestCommitHistoryForRepo(repo);
                            }
                        },
                        this::logError)
        );
    }

    public void requestCommitHistoryForRepo(Repo repo) {
        GitHubUser user = GitHubApi.getCurrentUser();
        GitHubApi.getService().getCommitActivity(user.getUsername(), repo.getName(), callback(
                        commitActivity -> bus.post(new CommitsReceived(repo, commitActivity)),
                        error -> Timber.i("Failed to retrieve commit history for " + repo.getName() + ": " + error.getMessage()))
        );
    }

    public void requestLanguageForRepo(final Repo repo) {
        GitHubUser user = GitHubApi.getCurrentUser();
        if (user == null) {
            return;
        }
        GitHubApi.getService().getLanguages(user.getUsername(), repo.getName(), callback(
                jsonElement -> {
                    List<Language> languages = GitHubUtils.parseLanguageResponse(jsonElement);
                    bus.post(new LanguagesReceived(repo, languages));
                    for (Language language : languages)
                        Timber.i("Language received: " + language.getName() + " : " + language.getBytes());
                },
                error -> logRepoError(repo, error)
        ));
    }

    public void requestGists() {
        GitHubApi.getService().getGists(callback(
                gists -> bus.post(new GistsReceived(gists)),
                this::logError
        ));
    }


    private boolean is404(Throwable error) {
        //  return error.getKind() == RetrofitError.Kind.HTTP &&
        //        error.getResponse().getStatus() == 404;
        return false;
    }

    private void logError(Throwable error) {
        Timber.i(error.getMessage());
    }

    private void logRepoError(Repo repo, Throwable error) {
        if (!is404(error))
            Timber.i("Failed to get repo: " + repo.getName());
        else
            Timber.i("Private repo could not be downloaded: " + repo.getName());
    }
}
