package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class DoneTasksPresenter extends BaseTasksPresenter<IBaseContract.IBaseView> implements IBaseContract.IBasePresenter{

    public DoneTasksPresenter(IBaseContract.IBaseView view, TaskRepository repository) {
        super(repository, view);
    }


    @Override
    public void loadData() {
        mView.showData(mRepository.getAllTasksByDone(true));
    }

    @Override
    public void onDelete(Task task) {
        task.setIsDeleted(true);
        mRepository.updateTask(task);
        mView.showData(mRepository.getAllTasksByDone(true));
        mView.showSnackbar();
    }

    @Override
    public void onDeleteCancel() {
        mRepository.processToDeleteTask(false);
        mView.showData(mRepository.getAllTasksByDone(true));
    }

    @Override
    public void onItemClick(Task task) {

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
    public void onIconClick(Task task, IBaseContract.IBaseTaskRow taskView) {
        if (task.getDateTo().after(new Date())) {
            task.setIsDone(false);
            mRepository.updateTask(task);
            taskView.animateMove(false);
            mNotificationScheduler.setAlarm(task.getTitle(), task.getDateTo(), task.isNotifAdded());
        }
    }

    @Override
    public void onAnimationEnd() {
        mView.showData(mRepository.getAllTasksByDone(true));
    }
}
