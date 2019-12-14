package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.presenter.CurrentTasksPresenter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.dialogfragments.AddTaskDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class CurrentTasksFragment extends BaseTaskFragment<ICurrentTasksContract.IPresenter> implements ICurrentTasksContract.IView,
        AddTaskDialogFragment.AddDialogListener {
    public static final String PAGE_TITLE = "Current";
    private static final String DIALOG_TAG = "AddTaskDialogFragment";
    private static final String TAG = "CurrentTasksFragment";
    private static final int FRAGMENT_TAG = 0;

    public CurrentTasksFragment() {
        super(R.layout.fragment_current_tasks);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = super.onCreateView(inflater, container, savedInstanceState);

        setPresenter();
        initRecyclerView(root);
        initFab(root);
        initSnackbar(root);
        return root;
    }

    private void setPresenter() {
        TaskRepository repository = new TaskRepository(requireContext());
        mPresenter = new CurrentTasksPresenter(this, repository);
    }

    private void initFab(View root) {
        FloatingActionButton fabAddTask = root.findViewById(R.id.fab_add);
        fabAddTask.setOnClickListener(v -> mPresenter.onAddClicked());
    }


    @Override
    public void openAddingDialog() {
        DialogFragment dialog = new AddTaskDialogFragment();
        dialog.setTargetFragment(this, FRAGMENT_TAG);
        dialog.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void openUpdateDialog(long id, String title, String date, boolean isNotifAdded) {
        Log.d(TAG, "openUpdateDialog() called with: id = [" + id + "], title = [" + title + "], date = [" + date + "], isNotifAdded = [" + isNotifAdded + "]");
        DialogFragment dialog = AddTaskDialogFragment.newInstance(id, title, date, isNotifAdded);
        dialog.setTargetFragment(this, FRAGMENT_TAG);
        dialog.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void onDialogPositiveClick(long id, String title, Date dateTime, boolean isNotifEnabled) {
        Log.d(TAG, "onDialogPositiveClick() called with: id = [" + id + "], title = [" + title + "], dateTime = [" + dateTime + "], isNotifEnabled = [" + isNotifEnabled + "]");
        mPresenter.onOkClicked(id, title, dateTime, isNotifEnabled);
    }

    @Override
    public void onDialogNegativeClick() {
        Toast.makeText(requireContext(), "Задача не добавлена", Toast.LENGTH_LONG).show();
    }
}
