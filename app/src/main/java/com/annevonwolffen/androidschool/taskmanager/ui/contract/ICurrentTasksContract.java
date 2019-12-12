package com.annevonwolffen.androidschool.taskmanager.ui.contract;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.Date;

public interface ICurrentTasksContract {
    interface IView extends IBaseContract.IBaseView {
        //        void showData();
        void openAddingDialog();

        void openUpdateDialog(long id, String title, String dateTime, boolean isNotifAdded);
//        void openDeletingDialog(int position);
//        void showSnackbar();
    }

//    interface ICurrentTaskRow extends IBaseContract.IBaseTaskRow {
////        void setTaskTitle(String title);
////        void setTaskDateTime(String dateTime);
////        void setIconColor(int color);
////        void setNotificationIcon(int color);
////        void setOnLongClickListener(int position);
//        void setOnClickItemListener(int position);
//    }

    interface IPresenter extends IBaseContract.IBasePresenter {
        //        void loadData();
        void onAddClicked(); // todo: vielleicht move to listener interface

        //        void onDelete(int position);
        void onOkClicked(long id, String title, Date dateTime, boolean isNotifEnabled);

        void insertTask(String title, Date date, boolean isNotif); // todo: maybe remove from interface

        //        void deleteTask();
//        void onDeleteCancel();


//        void onBindTaskRowViewAtPosition(int position, ICurrentTaskRow taskRow);
//        int getTasksRowsCount();
//        void onLongClick(int position);
//        void onItemClick(int position);
    }
}
