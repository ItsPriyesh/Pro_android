package io.prolabs.pro.ui.profile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import io.prolabs.pro.R;
import io.prolabs.pro.algo.FullUserStats;
import io.prolabs.pro.algo.UserXp;
import io.prolabs.pro.algo.XpCalculator;
import io.prolabs.pro.algo.XpCalculators;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.eventing.GitHubDataAggregator;
import io.prolabs.pro.eventing.GitHubReceiver;

public class XpActivity extends ActionBarActivity {
    private GitHubService gitHubService;
    private GitHubReceiver gitHubReceiver;
    private GitHubDataAggregator aggregator;
    private XpCalculator xpCalculator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xp);
        gitHubService = GitHubApi.getService();
        gitHubReceiver = GitHubReceiver.getInstance();
        xpCalculator = XpCalculators.SIMPLE;
        GitHubDataAggregator aggregator = new GitHubDataAggregator(gitHubReceiver);
        aggregator.register(this);
    }

    @Subscribe
    public synchronized void updateXp(FullUserStats stats) {
        UserXp xp = xpCalculator.calculateXp(stats);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_xp, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
