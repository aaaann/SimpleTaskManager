package com.annevonwolffen.androidschool.taskmanager.ui.view.util;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.annevonwolffen.androidschool.taskmanager.R;

public class ResourceWrapper {
    private final Resources mResources;

    public ResourceWrapper(@NonNull Resources resources) {
        mResources = resources;
    }

    public Drawable getTaskIcon() {
        return mResources.getDrawable(R.drawable.ic_check_circle_gray);
    }

    public Drawable getOverdueTaskIcon()  {
        return mResources.getDrawable(R.drawable.ic_overdue_red);
    }

    public Drawable getDoneTaskIcon()  {
        return mResources.getDrawable(R.drawable.ic_check_circle_green);
    }

    public Drawable getLongAgoDoneTaskIcon()  {
        return mResources.getDrawable(R.drawable.ic_check_circle_pale_green);
    }

    public Drawable getNotificationEnabledIcon()  {
        return mResources.getDrawable(R.drawable.ic_notifications_active_green);
    }
    public Drawable getNotificationDisabledIcon()  {
        return mResources.getDrawable(R.drawable.ic_notifications_none_black);
    }

    public int getOverdueTaskDateColor() {
        return mResources.getColor(R.color.colorOverdue);
    }

    public int getStandardTitleColor() {
        return mResources.getColor(R.color.colorGray1);
    }

    public int getStandardDateColor() {
        return mResources.getColor(R.color.colorGrayBlack);
    }

    public int getDoneTitleColor() {
        return mResources.getColor(R.color.colorGray2);
    }

}
