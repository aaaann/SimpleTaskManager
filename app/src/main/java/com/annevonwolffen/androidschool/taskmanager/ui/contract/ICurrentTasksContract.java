package com.annevonwolffen.androidschool.taskmanager.ui.contract;

public interface ICurrentTasksContract {
    interface IView {
        void showData();
        void openAddingDialog();
        void openDeletingDialog();
    }

    interface ICurrentTaskRow {
        void setTaskTitle(String title);
        void setTaskDateTime(String dateTime);
        void setIconColor(int color);
        void setNotificationIcon(int color);
    }

    interface IPresenter {
        void loadData();
        void onAddClicked(); // todo: vielleicht move to listener interface
        void insertTask();

        void onBindTaskRowViewAtPosition(int position, ICurrentTaskRow taskRow);
        int getTasksRowsCount();
    }
}
