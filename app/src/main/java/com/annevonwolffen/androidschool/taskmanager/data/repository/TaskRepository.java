package com.annevonwolffen.androidschool.taskmanager.data.repository;

import android.content.Context;

import com.annevonwolffen.androidschool.taskmanager.data.database.TaskDao;
import com.annevonwolffen.androidschool.taskmanager.data.database.TasksDb;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Класс для обращения к методам DAO
 */
public class TaskRepository {

    private final TaskDao mTaskDao;
    private final ExecutorService mExecutor;


    public TaskRepository(Context context) {
        TasksDb tasksDb = TasksDb.getDatabaseInstance(context);
        mTaskDao = tasksDb.taskDao();
        mExecutor = Executors.newSingleThreadExecutor();
    }

    public List<Task> getAllTasksByDone(boolean isDone) {
        List<Task> tasks  = Collections.emptyList();
        Future<List<Task>> fTasks = mExecutor.submit(new GetCallable(isDone));
        try {
            tasks = fTasks.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return Collections.unmodifiableList(tasks);
    }

    public Task getTaskById(long id) {
        Future<Task> fTask = mExecutor.submit(new GetOneCallable(id));
        try {
            return fTask.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }


    public long insertTask(Task task) {
        Future<Long> fId = mExecutor.submit(new InsertCallable(task));
        try {
            return fId.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return -1;
    }

    public int updateTask(Task task) {
        Future<Integer> fCount = mExecutor.submit(new UpdateCallable(task));
        try {
            return fCount.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int processToDeleteTask(boolean toDelete) {
        Future<Integer> fCount = mExecutor.submit(new DeleteCallable(toDelete));
        try {
            return fCount.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private class GetCallable implements Callable<List<Task>> {
        private boolean mIsDone;

        public GetCallable(boolean isDone) {
            mIsDone = isDone;
        }

        @Override
        public List<Task> call() {

            if (mIsDone)
                    return mTaskDao.findAllByIsDone();
                else
                    return mTaskDao.findAllByNotIsDone();
        }
    }

    private class GetOneCallable implements Callable<Task> {
        private long mId;

        public GetOneCallable(long id) {
            mId = id;
        }

        @Override
        public Task call() {
            return mTaskDao.findById(mId);
        }
    }

    private class InsertCallable implements Callable<Long> {
        private Task mTask;

        public InsertCallable(Task task) {
            mTask = task;
        }

        @Override
        public Long call() {
            return mTaskDao.insert(mTask);
        }
    }

    private class UpdateCallable implements Callable<Integer> {
        private Task mTask;

        public UpdateCallable(Task task) {
            mTask = task;
        }

        @Override
        public Integer call() {
            return mTaskDao.update(mTask);
        }
    }

    private class DeleteCallable implements Callable<Integer> {
        private boolean mToDelete;

        public DeleteCallable(boolean toDelete) {
            this.mToDelete = toDelete;
        }

        @Override
        public Integer call() {
            if (mToDelete) {
                return mTaskDao.delete();
            } else {
                return mTaskDao.unmarkIsDeleted();
            }
        }
    }


}
