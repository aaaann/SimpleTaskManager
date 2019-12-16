package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.AlarmReceiver;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IOnTaskOverdueListener;
import com.annevonwolffen.androidschool.taskmanager.ui.view.util.ResourceWrapper;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class CurrentTasksPresenter extends BaseTasksPresenter<ICurrentTasksContract.IView> implements ICurrentTasksContract.IPresenter,
        IOnTaskOverdueListener {

    public CurrentTasksPresenter(ICurrentTasksContract.IView view, TaskRepository repository, ResourceWrapper resourceWrapper) {
        super(repository, view, resourceWrapper);
        AlarmReceiver.bindListener(this);
        mNotificationScheduler.setAlarm(mRepository.getAllTasksByDone(false));
    }

    @Override
    public void loadData() {
        mView.showData(mRepository.getAllTasksByDone(false));
    }

    @Override
    public void onAddClicked() {
        mView.openAddingDialog();
    }

    @Override
    public void insertTask(String title, Date dateTime, boolean isNotifEnabled) {
        if (mRepository.insertTask(new Task(title, dateTime, isNotifEnabled)) != -1) {
            mNotificationScheduler.setAlarm(title, dateTime, isNotifEnabled);
            mView.showData(mRepository.getAllTasksByDone(false));
        }
    }

    @Override
    public void onEditDialogBtnClick(Task task) {
        mView.openUpdateDialog(task.getId(), task.getTitle(), dateToString(task.getDateTo()), task.isNotifAdded());
    }

    @Override
    public void onDoneDialogBtnClick(Task task) {
        task.setIsDone(true);
        mRepository.updateTask(task);
        mNotificationScheduler.removeAlarm(task.getDateTo());
        mView.showData(mRepository.getAllTasksByDone(false));
    }

    @Override
    public void onOverdueDialogBtnClick(Task task) {
        task.setIsOverdue(true);
        mRepository.updateTask(task);
        mNotificationScheduler.removeAlarm(task.getDateTo());
        mView.showData(mRepository.getAllTasksByDone(false));
    }

    private void updateTask(long id, String title, Date dateTime, boolean isNotifEnabled) {
        Task task = mRepository.getTaskById(id);
        Date prevDate = task.getDateTo();
        task.setTitle(title);
        task.setDateTo(dateTime);
        task.setIsNotifAdded(isNotifEnabled);
        if (mRepository.updateTask(task) > 0) {
            mNotificationScheduler.removeAlarm(prevDate);
            mNotificationScheduler.setAlarm(title, dateTime, isNotifEnabled);
            mView.showData(mRepository.getAllTasksByDone(false));
        }
    }


    @Override
    public void onDeleteCancel() {
        mRepository.processToDeleteTask(false);
        mView.showData(mRepository.getAllTasksByDone(false));
    }

    @Override
    public void onDelete(Task task) {
        task.setIsDeleted(true);
        mRepository.updateTask(task);
        mView.showData(mRepository.getAllTasksByDone(false));
        mView.showSnackbar();
    }

    @Override
    public void onBindTaskRowViewAtPosition(Task task, IBaseContract.IBaseTaskRow taskRow) {
        taskRow.setTaskTitle(task.getTitle());
        taskRow.setTaskDateTime(dateToString(task.getDateTo()));
        taskRow.setTaskDateColor(task.getDateTo().after(new Date()) ? mResourceWrapper.getStandardDateColor() : mResourceWrapper.getOverdueTaskDateColor());
        taskRow.setOnLongClickListener(task);
        taskRow.setOnClickItemListener(task);
        taskRow.setOnIconClickListener(task);
        taskRow.setTaskIcon(task.getDateTo().after(new Date()) ? mResourceWrapper.getTaskIcon() : mResourceWrapper.getOverdueTaskIcon());
        taskRow.setNotificationIcon(task.isNotifAdded() ? mResourceWrapper.getNotificationEnabledIcon() : mResourceWrapper.getNotificationDisabledIcon());
    }

    @Override
    public void onOkClicked(long id, String title, Date dateTime, boolean isNotifEnabled) {
        if (id == -1) {
            // insert
            insertTask(title, dateTime, isNotifEnabled);
        } else {
            // update
            updateTask(id, title, dateTime, isNotifEnabled);
        }
    }

    @Override
    public void onItemClick(Task task) {
        if (task.getDateTo().after(new Date())) {
            mView.openUpdateDialog(task.getId(), task.getTitle(), dateToString(task.getDateTo()), task.isNotifAdded());
        } else {
            mView.openMultipleChoiceDialog(task);
        }
    }

    @Override
    public void onOverdue() {
        loadData();
    }

    @Override
    public void onReboot() {
        mNotificationScheduler.setAlarm(mRepository.getAllTasksByDone(false));
    }

    @Override
    public void onIconClick(Task task, IBaseContract.IBaseTaskRow taskView) {
        if (task.getDateTo().after(new Date())) {
            task.setIsDone(true);
            mRepository.updateTask(task);
            mNotificationScheduler.removeAlarm(task.getDateTo());
            taskView.animateMove(true);
        }
    }

    @Override
    public void onAnimationEnd() {
        mView.showData(mRepository.getAllTasksByDone(false));
    }
}
