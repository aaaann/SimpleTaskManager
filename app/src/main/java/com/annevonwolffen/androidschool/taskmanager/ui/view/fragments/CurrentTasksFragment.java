package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.presenter.CurrentTasksPresenter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.adapters.CurrentTasksAdapter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.dialogfragments.AddTaskDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class CurrentTasksFragment extends Fragment implements ICurrentTasksContract.IView,
        AddTaskDialogFragment.AddDialogListener {
    public static final String PAGE_TITLE = "Current";
    private static final String DIALOG_TAG = "AddTaskDialogFragment";
    private static final int FRAGMENT_TAG = 0;

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
        View root = super.onCreateView(inflater, container, savedInstanceState);

        setPresenter();
        initRecyclerView(root);
        initFab(root);
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

    private void initFab(View root) {
        FloatingActionButton fabAddTask = root.findViewById(R.id.fab_add);
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.onAddClicked();
            }
        });
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
        DialogFragment dialog = new AddTaskDialogFragment();
        dialog.setTargetFragment(this, FRAGMENT_TAG);
        dialog.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void openDeletingDialog() {

    }

    @Override
    public void onDialogPositiveClick(String title, Date dateTime, boolean isNotifEnabled) {
        mPresenter.insertTask(title, dateTime, isNotifEnabled);
    }

    @Override
    public void onDialogNegativeClick() {
        Toast.makeText(requireContext(),"Задача не добавлена", Toast.LENGTH_LONG).show();
    }
}
