package io.prolabs.pro.eventing;

import java.util.List;

import io.prolabs.pro.models.github.Gist;

/**
 * Created by Edmund on 2015-03-08.
 */
public class GistsReceived {
    private List<Gist> gists;

    public List<Gist> getGists() {
        return gists;
    }

    public GistsReceived(List<Gist> gists) {
        this.gists = gists;
    }
}
