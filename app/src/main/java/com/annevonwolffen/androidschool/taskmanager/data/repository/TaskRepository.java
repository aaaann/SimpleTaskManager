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


    public TaskRepository(Context context){
        TasksDb tasksDb = TasksDb.getDatabaseInstance(context);
        mTaskDao = tasksDb.taskDao();
    }

    public void getAllTasksByDone(final boolean isDone, final OnDbOperationListener onDbOperationListener) {
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

                onDbOperationListener.onFinish(tasks);
            }
        }.execute();
    }

    public List<Task> getAllTasksDone() {
        return mTaskDao.findAllByIsDone();
    }

    public void insertTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mTaskDao.insert(task);
                return null;
            }
        }.execute();
    }

    public void updateTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mTaskDao.update(task);
                return null;
            }
        }.execute();
    }

    public void deleteTask(final Task task) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                mTaskDao.delete(task);
                return null;
            }
        }.execute();
    }


    public interface OnDbOperationListener{
        void onFinish(List<Task> tasks);
    }
}
