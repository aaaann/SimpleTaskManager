package com.annevonwolffen.androidschool.taskmanager.ui.view.adapters;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
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

    class CurrentTaskViewHolder extends RecyclerView.ViewHolder implements IBaseContract.IBaseTaskRow {

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
        public void setTitleColor(int color) {
            mTitle.setTextColor(color);
        }

        @Override
        public void setTaskDateTime(String dateTime) {
            mDate.setText(dateTime);
        }

        @Override
        public void setTaskIcon(Drawable drawable) {
            mIcon.setImageDrawable(drawable);
        }

        @Override
        public void setNotificationIcon(Drawable drawable) {
            mNotificationIcon.setImageDrawable(drawable);
        }

        @Override
        public void setTaskDateColor(int color) {
            mDate.setTextColor(color);
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

        @Override
        public void setOnIconClickListener(Task task) {
            mIcon.setOnClickListener(v -> mPresenter.onIconClick(task, this));
        }

        @Override
        public void animateMove(boolean moveForward) {
            //добавляем анимацию - поворот картинке вокруг вертикальной оси
            ObjectAnimator flipIn = ObjectAnimator.ofFloat(mIcon,
                    "rotationY",
                    -180f, 0f);
            flipIn.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    //аниматор, который передвигает элемент списка по горизонтали
                    //на величину равную его длине
                    //таким образом, он исчезает из поля зрения
                    ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                            "translationX", 0f, moveForward ? itemView.getWidth() : -itemView.getWidth());
                    //другой аниматор, возвращающий элемент списка в исходное состояние
                    ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                            "translationX", moveForward ? itemView.getWidth() : -itemView.getWidth(), 0f);

                    translationX.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            //делаем элемент невидимым, чтобы он не отображался после удаления
//                            itemView.setVisibility(View.GONE);
                            mPresenter.onAnimationEnd();

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    AnimatorSet translationSet = new AnimatorSet();
                    translationSet.play(translationX).before(translationXBack);
                    translationSet.start();
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            flipIn.start();
        }
    }
}
