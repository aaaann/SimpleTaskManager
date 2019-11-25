package com.annevonwolffen.androidschool.taskmanager.data.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from Task where is_done = 1")
    List<Task> findAllByIsDone();

    @Query("select * from Task where is_done = 0")
    List<Task> findAllByNotIsDone();

    @Insert
    long insert(Task task);

    @Update
    int update(Task task);

    @Delete
    int delete(Task task);
}
