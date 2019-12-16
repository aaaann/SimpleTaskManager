package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.annevonwolffen.androidschool.taskmanager.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    private static final String ARG_ROOT = "rootScreen";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

    }
}
