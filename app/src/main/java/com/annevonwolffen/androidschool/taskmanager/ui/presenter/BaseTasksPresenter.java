package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;
import com.annevonwolffen.androidschool.taskmanager.ui.view.util.ResourceWrapper;

public abstract class BaseTasksPresenter<T extends IBaseContract.IBaseView> implements IBaseContract.IBasePresenter {

    protected final TaskRepository mRepository;
    protected T mView;
    protected NotificationScheduler mNotificationScheduler;
    protected final ResourceWrapper mResourceWrapper;

    public BaseTasksPresenter(TaskRepository repository, T view, @NonNull ResourceWrapper resourceWrapper) {
        mRepository = repository;
        mView = view;
        mNotificationScheduler = NotificationScheduler.getInstance();
        mResourceWrapper = resourceWrapper;
    }

    @Override
    public void subscribe(@NonNull IBaseContract.IBaseView view) {

    }

    @Override
    public void unsubscribe() {
        mView = null;
    }


    /**
     * окончательное удаление таски из базы
     */
    @Override
    public void onSnackbarDismissed() {
        Task task = mRepository.getDeletedTask();
        mNotificationScheduler.removeAlarm(task.getDateTo());
        mRepository.processToDeleteTask(true);
    }

    @Override
    public void onLongClick(Task task) {
        mView.openDeletingDialog(task);
    }

}
