package com.annevonwolffen.androidschool.taskmanager.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IBaseContract;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.CurrentTaskViewHolder> {

    private final IBaseContract.IBasePresenter mPresenter;
    private List<Task> mCurrentTasks = new ArrayList<>();

    public TasksAdapter(IBaseContract.IBasePresenter presenter) {
        mPresenter = presenter;
    }


    @NonNull
    @Override
    public CurrentTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrentTaskViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_item, parent, false), mPresenter);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentTaskViewHolder holder, int position) {
        mPresenter.onBindTaskRowViewAtPosition(mCurrentTasks.get(position), holder);
    }

    public void setTasks(List<Task> tasks) {
        mCurrentTasks = tasks;
    }

    @Override
    public int getItemCount() {
        return mCurrentTasks.size();
    }

    static class CurrentTaskViewHolder extends RecyclerView.ViewHolder implements IBaseContract.IBaseTaskRow {

        private ImageView mIcon;
        private TextView mTitle;
        private TextView mDate;
        private ImageView mNotificationIcon;
        private View mView;
        private final IBaseContract.IBasePresenter mPresenter;

        public CurrentTaskViewHolder(@NonNull View itemView, IBaseContract.IBasePresenter presenter) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.iv_icon);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDate = itemView.findViewById(R.id.tv_date);
            mNotificationIcon = itemView.findViewById(R.id.iv_is_notif_icon);
            mView = itemView;
            mPresenter = presenter;
        }


        @Override
        public void setTaskTitle(String title) {
            mTitle.setText(title);
        }

        @Override
        public void setTaskDateTime(String dateTime) {
            mDate.setText(dateTime);
        }

        @Override
        public void setIconColor(int color) {
            // todo: implement (change icon image in xml file)
        }

        @Override
        public void setNotificationIcon(int color) {
            // todo: implement
        }

        @Override
        public void setOnLongClickListener(Task task) {
            mView.setOnLongClickListener(v -> {
                mPresenter.onLongClick(task);
                return true;
            });
        }

        @Override
        public void setOnClickItemListener(Task task) {
            mView.setOnClickListener(v -> mPresenter.onItemClick(task));
        }
    }
}
