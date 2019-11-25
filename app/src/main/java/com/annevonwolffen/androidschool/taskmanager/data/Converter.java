package com.annevonwolffen.androidschool.taskmanager.data;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converter {
    @TypeConverter
    public long fromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date toDate(long millis) {
        return new Date(millis);
    }

    public int booleanToInt(boolean b) {
        return b ? 1 : 0;
    }

    public boolean intToBoolean(int i) {
        return i == 1;
    }
}
