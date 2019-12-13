package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public abstract class BaseTasksPresenter<T extends IBaseContract.IBaseView> implements IBaseContract.IBasePresenter {

    protected final TaskRepository mRepository;
    protected T mView;

    public BaseTasksPresenter(TaskRepository repository, T view) {
        mRepository = repository;
        mView = view;
    }

    @Override
    public void subscribe(@NonNull IBaseContract.IBaseView view) {

    }

    @Override
    public void unsubscribe() {
        mView = null;
    }


    @Override
    public void onSnackbarDismissed() {
        mRepository.processToDeleteTask(true);
    }

    @Override
    public void onBindTaskRowViewAtPosition(Task task, IBaseContract.IBaseTaskRow taskRow) {
        taskRow.setTaskTitle(task.getTitle());
        taskRow.setTaskDateTime(dateToString(task.getDateTo()));
        taskRow.setOnLongClickListener(task);
        taskRow.setOnClickItemListener(task);
        taskRow.setOnIconClickListener(task);
        //todo: set icons
    }

    @Override
    public void onLongClick(Task task) {
        mView.openDeletingDialog(task);
    }

}
