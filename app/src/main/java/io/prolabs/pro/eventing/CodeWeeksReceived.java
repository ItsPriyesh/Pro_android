package io.prolabs.pro.eventing;

import java.util.List;

import io.prolabs.pro.models.github.CodeWeek;
import io.prolabs.pro.models.github.Repo;

/**
 * Created by Edmund on 2015-03-08.
 */
public class CodeWeeksReceived {
    private List<CodeWeek> codeWeeks;
    private Repo repo;

    public CodeWeeksReceived(Repo repo, List<CodeWeek> codeWeeks) {
        this.codeWeeks = codeWeeks;
        this.repo = repo;
    }

    public List<CodeWeek> getCodeWeeks() {
        return codeWeeks;
    }

    public Repo getRepo() {
        return repo;
    }
}
