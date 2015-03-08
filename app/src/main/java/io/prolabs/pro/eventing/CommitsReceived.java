package io.prolabs.pro.eventing;

import java.util.List;

import io.prolabs.pro.models.github.CommitActivity;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class CommitsReceived {
    private final Repo repo;
    private final List<CommitActivity> commitActivity;

    public CommitsReceived(Repo repo, List<CommitActivity> commitActivity) {
        this.repo = repo;
        this.commitActivity = commitActivity;
    }

    public Repo getRepo() {
        return repo;
    }

    public List<CommitActivity> getCommitActivity() {
        return commitActivity;
    }
}
