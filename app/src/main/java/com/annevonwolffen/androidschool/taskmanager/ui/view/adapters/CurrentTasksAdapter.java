package com.annevonwolffen.androidschool.taskmanager.ui.view.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.ICurrentTasksContract;

public class CurrentTasksAdapter extends RecyclerView.Adapter<CurrentTasksAdapter.CurrentTaskViewHolder> {

    private ICurrentTasksContract.IPresenter mPresenter;

    public CurrentTasksAdapter(ICurrentTasksContract.IPresenter presenter) {
        mPresenter = presenter;
    }


    @NonNull
    @Override
    public CurrentTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CurrentTaskViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.task_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentTaskViewHolder holder, int position) {
        mPresenter.onBindTaskRowViewAtPosition(position, holder);
    }

    @Override
    public int getItemCount() {
        return mPresenter.getTasksRowsCount();
    }

    static class CurrentTaskViewHolder extends RecyclerView.ViewHolder implements ICurrentTasksContract.ICurrentTaskRow {

        private ImageView mIcon;
        private TextView mTitle;
        private TextView mDate;
        private ImageView mNotificationIcon;

        public CurrentTaskViewHolder(@NonNull View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.iv_icon);
            mTitle = itemView.findViewById(R.id.tv_title);
            mDate = itemView.findViewById(R.id.tv_date);
            mNotificationIcon = itemView.findViewById(R.id.iv_is_notif_icon);
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
    }
}
