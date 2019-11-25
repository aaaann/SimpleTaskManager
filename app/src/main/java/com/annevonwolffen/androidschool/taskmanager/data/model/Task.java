package com.annevonwolffen.androidschool.taskmanager.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class Task {

    @Ignore
    public Task(String title, Date date, boolean isNotifAdded) {
        mTitle = title;
        mDateTo = date;
        mIsNotifAdded = isNotifAdded;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "date_to")
    private Date mDateTo;

    @ColumnInfo(name = "is_done", defaultValue = "false")
    private boolean mIsDone;

    @ColumnInfo(name = "is_notif_added", defaultValue = "true")
    private boolean mIsNotifAdded;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        this.mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getDateTo() {
        return mDateTo;
    }

    public void setDateTo(Date dateTo) {
        this.mDateTo = dateTo;
    }

    public boolean isDone() {
        return mIsDone;
    }

    public void setIsDone(boolean isDone) {
        this.mIsDone = isDone;
    }

    public boolean isNotifAdded() {
        return mIsNotifAdded;
    }

    public void setIsNotifAdded(boolean isNotifAdded) {
        this.mIsNotifAdded = isNotifAdded;
    }
}
