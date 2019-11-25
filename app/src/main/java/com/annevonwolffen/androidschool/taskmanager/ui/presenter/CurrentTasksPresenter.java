package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import android.content.Context;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class CurrentTasksPresenter implements ICurrentTasksContract.IPresenter,
        TaskRepository.OnDbOperationListener {
    private final TaskRepository mRepository;
    private final ICurrentTasksContract.IView mView;
    private List<Task> mCurrentTasks = new ArrayList<>();

    public CurrentTasksPresenter(ICurrentTasksContract.IView view, TaskRepository repository) { // todo: remove context from presenter, create repository in view (fragment)
        mView = view;
        mRepository = repository;
    }


    @Override
    public void loadData() {
        mRepository.getAllTasksByDone(false, this);;
        mView.showData();
    }

    @Override
    public void onAddClicked() {
        mView.openAddingDialog();
    }

    @Override
    public void insertTask(String title, Date dateTime, boolean isNotifEnabled) {
        mRepository.insertTask(new Task(title, dateTime, isNotifEnabled), this);
    }

    @Override
    public void onBindTaskRowViewAtPosition(int position, ICurrentTasksContract.ICurrentTaskRow taskRow) {
        Task task = mCurrentTasks.get(position);
        taskRow.setTaskTitle(task.getTitle());
        taskRow.setTaskDateTime(dateToString(task.getDateTo()));
        //todo: set icons
    }

    @Override
    public int getTasksRowsCount() {
        return mCurrentTasks.size();
    }

    @Override
    public void onFinish(List<Task> tasks) {
        mCurrentTasks = tasks;
        mView.showData();
    }

    @Override
    public void onFinish(long id, Task task) {
        if (id != -1) {

            task.setId(id);
            mCurrentTasks.add(task);
            mView.showData();
        }
    }

    @Override
    public void onFinish(int count) {

    }
}
