package com.annevonwolffen.androidschool.taskmanager.ui.contract;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.Date;

public interface ICurrentTasksContract {
    interface IView extends IBaseContract.IBaseView {
        void openAddingDialog();
        void openUpdateDialog(long id, String title, String dateTime, boolean isNotifAdded);
        void openMultipleChoiceDialog(Task task);
    }

    interface IPresenter extends IBaseContract.IBasePresenter {
        void onAddClicked();
        void onOkClicked(long id, String title, Date dateTime, boolean isNotifEnabled);
        void insertTask(String title, Date date, boolean isNotif);
        void onEditDialogBtnClick(Task task);
        void onDoneDialogBtnClick(Task task);
        void onOverdueDialogBtnClick(Task task);
    }
}
