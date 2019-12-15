package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.alarm.AlarmReceiver;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IOnTaskOverdueListener;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class CurrentTasksPresenter extends BaseTasksPresenter<ICurrentTasksContract.IView> implements ICurrentTasksContract.IPresenter,
        IOnTaskOverdueListener {

    public CurrentTasksPresenter(ICurrentTasksContract.IView view, TaskRepository repository) {
        super(repository, view);
        AlarmReceiver.bindListener(this);
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
        mView.openUpdateDialog(task.getId(), task.getTitle(), dateToString(task.getDateTo()), task.isNotifAdded());
    }

    @Override
    public void onOverdue() {
        loadData();
    }

    @Override
    public void onReboot() {
        mNotificationScheduler.setAlarm(mRepository.getAllTasksByDone(false));
    }
}
