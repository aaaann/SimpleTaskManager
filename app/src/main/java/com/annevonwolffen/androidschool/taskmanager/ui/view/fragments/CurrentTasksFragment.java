package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.AlarmReceiver;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.presenter.CurrentTasksPresenter;
import com.annevonwolffen.androidschool.taskmanager.ui.view.dialogfragments.AddTaskDialogFragment;
import com.annevonwolffen.androidschool.taskmanager.ui.view.util.ResourceWrapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Date;

public class CurrentTasksFragment extends BaseTaskFragment<ICurrentTasksContract.IPresenter> implements ICurrentTasksContract.IView,
        AddTaskDialogFragment.AddDialogListener {
    public static String PAGE_TITLE = "Текущие";
    private static final String DIALOG_TAG = "AddTaskDialogFragment";
    private static final int FRAGMENT_TAG = 0;

    private AlarmReceiver mAlarmReceiver;

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
        setUpBroadcastReceiver();
        ResourceWrapper mResourcseWrapper = new ResourceWrapper(requireContext().getResources());
        mPresenter = new CurrentTasksPresenter(this, repository, mResourcseWrapper);
    }

    private void setUpBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);

        mAlarmReceiver = new AlarmReceiver();

        requireContext().registerReceiver(mAlarmReceiver, filter);
    }

    private void destroyBroadcastReceiver() {
        requireContext().unregisterReceiver(mAlarmReceiver);
        mAlarmReceiver = null;
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
        DialogFragment dialog = AddTaskDialogFragment.newInstance(id, title, date, isNotifAdded);
        dialog.setTargetFragment(this, FRAGMENT_TAG);
        dialog.show(getFragmentManager(), DIALOG_TAG);
    }

    @Override
    public void openMultipleChoiceDialog(Task task) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setMessage(R.string.task_is_overdue);

        dialogBuilder.setNeutralButton(R.string.dialog_btn_text_edit, (dialog, which) -> mPresenter.onEditDialogBtnClick(task));
        dialogBuilder.setPositiveButton(R.string.dialog_btn_text_done, (dialog, which) -> mPresenter.onDoneDialogBtnClick(task));
        dialogBuilder.setNegativeButton(R.string.dialog_btn_text_overdue, (dialog, which) -> mPresenter.onOverdueDialogBtnClick(task));
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void onDialogPositiveClick(long id, String title, Date dateTime, boolean isNotifEnabled) {
        mPresenter.onOkClicked(id, title, dateTime, isNotifEnabled);
    }

    @Override
    public void onDialogNegativeClick() {
        Toast.makeText(requireContext(), getString(R.string.canceled), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyBroadcastReceiver();
        mPresenter.unsubscribe();
    }
}
