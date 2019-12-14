package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler;
import com.annevonwolffen.androidschool.taskmanager.ui.view.adapters.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class TasksFragment extends Fragment {

    private TabAdapter mTabAdapter;

    public TasksFragment() {
        super(R.layout.fragment_tasks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        NotificationScheduler.getInstance().init(requireContext());

        mTabAdapter = new TabAdapter(getFragmentManager(), 2);
        ViewPager viewPager = root.findViewById(R.id.pager);
        viewPager.setAdapter(mTabAdapter);
        TabLayout tabLayout = root.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        return root;
    }
}
