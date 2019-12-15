package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public abstract class BaseTasksPresenter<T extends IBaseContract.IBaseView> implements IBaseContract.IBasePresenter {

    protected final TaskRepository mRepository;
    protected T mView;
    protected NotificationScheduler mNotificationScheduler;

    public BaseTasksPresenter(TaskRepository repository, T view) {
        mRepository = repository;
        mView = view;
        mNotificationScheduler = NotificationScheduler.getInstance();
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
    public void onBindTaskRowViewAtPosition(Task task, IBaseContract.IBaseTaskRow taskRow) {
        taskRow.setTaskTitle(task.getDateTo().before(new Date()) ? task.getTitle() + "!!!OVERDUE!!!" : task.getTitle());
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
