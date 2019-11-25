package com.annevonwolffen.androidschool.taskmanager.ui.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtils {
    public static String dateToString(Date date) {
        DateFormat simple = new SimpleDateFormat("dd.MMM.yyyy HH:mm");
        return simple.format(date);

    }
}
