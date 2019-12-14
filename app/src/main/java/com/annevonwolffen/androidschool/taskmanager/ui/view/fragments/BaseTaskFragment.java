package com.annevonwolffen.androidschool.taskmanager.ui.view.fragments;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;
import com.annevonwolffen.androidschool.taskmanager.ui.view.adapters.TasksAdapter;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


public abstract class BaseTaskFragment<T extends IBaseContract.IBasePresenter> extends Fragment implements IBaseContract.IBaseView {

    protected T mPresenter;
    protected TasksAdapter mAdapter;
    protected Snackbar mSnackbar;

    public BaseTaskFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
//        NotificationScheduler.getInstance().init(requireContext());
    }


    protected void initRecyclerView(View view) {
        LinearLayoutManager layoutManager = new
                LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false);
        mAdapter = new TasksAdapter(mPresenter);
        RecyclerView mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
    }

    protected void initSnackbar(View root) {
        mSnackbar = Snackbar.make(root.findViewById(R.id.root_coordinator),
                "Удален 1 элемент", Snackbar.LENGTH_LONG)
                .setCallback(new Snackbar.Callback() {
                                 @Override
                                 public void onDismissed(Snackbar snackbar, int event) {
                                     if (event == BaseTransientBottomBar.BaseCallback.DISMISS_EVENT_TIMEOUT) {
                                         mPresenter.onSnackbarDismissed();
                                     }
                                 }
                             }
                )
                .setAction("ОТМЕНА", v -> {
                    Toast.makeText(requireContext(), "Удаление отменено", Toast.LENGTH_LONG).show();
                    mPresenter.onDeleteCancel();
                });
    }

    @Override
    public void showData(List<Task> tasks) {
        mAdapter.setTasks(tasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void openDeletingDialog(Task task) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
        dialogBuilder.setMessage("Удалить выбранный элемент?");

        dialogBuilder.setPositiveButton("ОК", (dialog, which) -> {
            mPresenter.onDelete(task);
        });
        AlertDialog dialog = dialogBuilder.create();
        dialog.show();
    }

    @Override
    public void showSnackbar() {
        mSnackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();

        mPresenter.loadData();
    }


}
