package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class CurrentTasksPresenter extends BaseTasksPresenter<ICurrentTasksContract.IView> implements ICurrentTasksContract.IPresenter {

    public CurrentTasksPresenter(ICurrentTasksContract.IView view, TaskRepository repository) {
        super(repository, view);
    }

    @Override
    public void subscribe(@NonNull IBaseContract.IBaseView view) {

    }

    @Override
    public void unsubscribe() {
        mView = null;
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
            mView.showData(mRepository.getAllTasksByDone(false));
        }
    }

    private void updateTask(long id, String title, Date dateTime, boolean isNotifEnabled) {
        Task task = mRepository.getTaskById(id);
        task.setTitle(title);
        task.setDateTo(dateTime);
        task.setIsNotifAdded(isNotifEnabled);
        if (mRepository.updateTask(task) > 0) {
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


//    @Override
//    public void onBindTaskRowViewAtPosition(Task task, IBaseContract.IBaseTaskRow taskRow) {
//        taskRow.setTaskTitle(task.getTitle());
//        taskRow.setTaskDateTime(dateToString(task.getDateTo()));
//        taskRow.setOnLongClickListener(task);
//        taskRow.setOnClickItemListener(task);
//        //todo: set icons
//    }


    @Override
    public void onItemClick(Task task) {
        mView.openUpdateDialog(task.getId(), task.getTitle(), dateToString(task.getDateTo()), task.isNotifAdded());
    }

    @Override
    public void onIconClick(Task task, IBaseContract.IBaseTaskRow taskView) {
        task.setIsDone(true);
        mRepository.updateTask(task);
    }

    @Override
    public void onAnimationEnd() {
        mView.showData(mRepository.getAllTasksByDone(false));
    }

}
