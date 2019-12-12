package com.annevonwolffen.androidschool.taskmanager.ui.presenter;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.data.repository.TaskRepository;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;

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
}
