package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.annevonwolffen.androidschool.taskmanager.R;

public class CurrentTasksFragment extends Fragment {
    public static final String PAGE_TITLE = "Current";

    public CurrentTasksFragment() {
        super(R.layout.fragment_current_tasks);
    }

    public static CurrentTasksFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CurrentTasksFragment fragment = new CurrentTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
