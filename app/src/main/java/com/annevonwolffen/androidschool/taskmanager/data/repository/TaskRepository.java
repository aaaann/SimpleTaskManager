package com.annevonwolffen.androidschool.taskmanager.data.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.annevonwolffen.androidschool.taskmanager.data.database.TaskDao;
import com.annevonwolffen.androidschool.taskmanager.data.database.TasksDb;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.List;

/**
 * Класс для обращения к методам DAO
 */
public class TaskRepository {

    private final TaskDao mTaskDao;


    public TaskRepository(Context context) {
        TasksDb tasksDb = TasksDb.getDatabaseInstance(context);
        mTaskDao = tasksDb.taskDao();
    }

    public void getAllTasksByDone(final boolean isDone, final OnDbOperationListener listener) {
        new AsyncTask<Void, Void, List<Task>>() {
            @Override
            protected List<Task> doInBackground(Void... voids) {
                if (isDone)
                    return mTaskDao.findAllByIsDone();
                else
                    return mTaskDao.findAllByNotIsDone();
            }

            @Override
            protected void onPostExecute(List<Task> tasks) {
                super.onPostExecute(tasks);

                listener.onFinish(tasks);
            }
        }.execute();
    }


    public void insertTask(final Task task, final OnDbOperationListener listener) {
        new AsyncTask<Void, Void, Long>() {

            @Override
            protected Long doInBackground(Void... voids) {
                return mTaskDao.insert(task);
            }

            @Override
            protected void onPostExecute(Long id) {
                super.onPostExecute(id);

                listener.onFinish(id, task);
            }
        }.execute();
    }

    public void updateTask(final Task task, final OnDbOperationListener listener) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return mTaskDao.update(task);
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);

                listener.onFinish(count);
            }
        }.execute();
    }

    public void deleteTask(final Task task, final OnDbOperationListener listener) {
        new AsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackground(Void... voids) {
                return mTaskDao.delete(task);
            }

            @Override
            protected void onPostExecute(Integer count) {
                super.onPostExecute(count);

                listener.onFinish(count);
            }
        }.execute();
    }


    public interface OnDbOperationListener {
        void onFinish(List<Task> tasks);

        void onFinish(long id, Task task);

        void onFinish(int count);
    }
}
