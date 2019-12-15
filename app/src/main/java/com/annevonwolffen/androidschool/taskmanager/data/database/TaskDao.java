package com.annevonwolffen.androidschool.taskmanager.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.List;

@Dao
public interface TaskDao {

    @Query("select * from Task where is_done = 1 and is_deleted = 0")
    List<Task> findAllByIsDone();

    @Query("select * from Task where is_done = 0 and is_deleted = 0")
    List<Task> findAllByNotIsDone();

    @Query("select * from Task where id = :id")
    Task findById(Long id);

    @Query("select * from Task where is_deleted = 1 limit 1")
    Task findByIsDeletedTrue();

    @Insert
    long insert(Task task);

    @Update
    int update(Task task);

    @Query("update Task set is_deleted = 0 where is_deleted = 1")
    int unmarkIsDeleted();

    @Query("delete from Task where is_deleted = 1")
    int delete();
}
