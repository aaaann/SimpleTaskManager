package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;
import com.annevonwolffen.androidschool.taskmanager.ui.presenter.DoneTasksPresenter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.util.ResourceWrapper;

public class DoneTasksFragment extends BaseTaskFragment<IBaseContract.IBasePresenter> {
    public static String PAGE_TITLE = "Сделанные";

    public DoneTasksFragment() {
        super(R.layout.fragment_done_tasks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        setPresenter();
        initRecyclerView(root);
        initSnackbar(root);
        return root;
    }

    private void setPresenter() {
        TaskRepository repository = new TaskRepository(requireContext());
        ResourceWrapper mResourcseWrapper = new ResourceWrapper(requireContext().getResources());
        mPresenter = new DoneTasksPresenter(this, repository, mResourcseWrapper);
    }
}
