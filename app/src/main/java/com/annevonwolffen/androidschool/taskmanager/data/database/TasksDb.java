package com.annevonwolffen.androidschool.taskmanager.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.annevonwolffen.androidschool.taskmanager.data.Converter;
import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

@Database(entities = {Task.class}, version = 1)
@TypeConverters(Converter.class)
public abstract class TasksDb extends RoomDatabase {
    public abstract TaskDao taskDao();
    private static TasksDb INSTANCE;

    public static TasksDb getDatabaseInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (TasksDb.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TasksDb.class, "tasks.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public static void destroyDatabaseInstance() {
        INSTANCE = null;
    }

}
