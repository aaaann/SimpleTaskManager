package com.annevonwolffen.androidschool.taskmanager.ui.view.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.annevonwolffen.androidschool.taskmanager.ui.view.fragments.CurrentTasksFragment;
import com.annevonwolffen.androidschool.taskmanager.ui.view.fragments.DoneTasksFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private int mNumberOfPages;
    private CurrentTasksFragment mCurrentTasksFragment;
    private DoneTasksFragment mDoneTasksFragment;

    public TabAdapter(@NonNull FragmentManager fm, int numberOfPages) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        mNumberOfPages = numberOfPages;
        mCurrentTasksFragment = new CurrentTasksFragment();
        mDoneTasksFragment = new DoneTasksFragment();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 1) {
            return mDoneTasksFragment;
        }
        return mCurrentTasksFragment;
    }

    @Override
    public int getCount() {
        return mNumberOfPages;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 1) {
            return DoneTasksFragment.PAGE_TITLE;
        }
        return CurrentTasksFragment.PAGE_TITLE;
    }
}
