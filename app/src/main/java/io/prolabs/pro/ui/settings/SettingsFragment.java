package io.prolabs.pro.ui.settings;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.ActivityCompat;

import io.prolabs.pro.R;
import io.prolabs.pro.api.github.GitHubApi;
import io.prolabs.pro.api.github.GitHubService;
import io.prolabs.pro.ui.MainActivity;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);

        Preference logout = findPreference("logout");
        logout.setOnPreferenceClickListener(pref -> {
            new AlertDialog.Builder(getActivity())
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("Yes", (dialog, id) -> logout())
                    .setNegativeButton("No", (dialog, id) -> dialog.dismiss())
                    .create().show();
            return true;
        });
    }

    private void logout() {
        GitHubApi.clearCurrentAuth();
        GitHubApi.deleteService();
        startActivity(new Intent(getActivity(), MainActivity.class));
        ActivityCompat.finishAffinity(getActivity());
    }
}