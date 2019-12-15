package com.annevonwolffen.androidschool.taskmanager.ui.contract;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.List;

public interface IBaseContract {

    interface IBaseView {
        void showData(List<Task> tasks);
        void openDeletingDialog(Task task);
        void showSnackbar();
        void refreshTabFragment();
    }

    interface IBasePresenter<V extends IBaseView> {
        void subscribe(@NonNull V view);
        void unsubscribe();
        void loadData();
        void onDelete(Task task);
        void onDeleteCancel();
        void onSnackbarDismissed();
        void onBindTaskRowViewAtPosition(Task task, IBaseTaskRow taskRow);
        void onLongClick(Task task);
        void onItemClick(Task task);
        void onIconClick(Task task, IBaseContract.IBaseTaskRow taskRow);
        void onAnimationEnd();
    }

    interface IBaseTaskRow {
        void setTaskTitle(String title);
        void setTaskDateTime(String dateTime);
        void setIconColor(int color);
        void setNotificationIcon(int color);
        void setOnLongClickListener(Task task);
        void setOnClickItemListener(Task task);
        void setOnIconClickListener(Task task);
        void animateMove(boolean animateForward);
    }
}
