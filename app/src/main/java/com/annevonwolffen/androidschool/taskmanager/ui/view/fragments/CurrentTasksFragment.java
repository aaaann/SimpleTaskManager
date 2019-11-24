package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.presenter.CurrentTasksPresenter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.adapters.CurrentTasksAdapter;

public class CurrentTasksFragment extends Fragment implements ICurrentTasksContract.IView {
    public static final String PAGE_TITLE = "Current";

    private ICurrentTasksContract.IPresenter mPresenter;
    private CurrentTasksAdapter mAdapter;

    public CurrentTasksFragment() {
        super(R.layout.fragment_current_tasks);
    }

    public static CurrentTasksFragment newInstance() {
        
        Bundle args = new Bundle();
        
        CurrentTasksFragment fragment = new CurrentTasksFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root =  super.onCreateView(inflater, container, savedInstanceState);

        setPresenter();
        initRecyclerView(root);
        return root;
    }

    private void setPresenter() {
        TaskRepository repository = new TaskRepository(requireContext());
        mPresenter = new CurrentTasksPresenter(this, repository);
    }

    private void initRecyclerView(View view) {
        LinearLayoutManager layoutManager = new
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        mAdapter = new CurrentTasksAdapter(mPresenter);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.loadData();
    }

    @Override
    public void showData() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openAddingDialog() {

    }

    @Override
    public void openDeletingDialog() {

    }
}
