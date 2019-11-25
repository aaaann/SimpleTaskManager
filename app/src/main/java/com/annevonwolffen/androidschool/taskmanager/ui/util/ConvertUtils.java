package com.annevonwolffen.androidschool.taskmanager.ui.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConvertUtils {
    public static String dateToString(Date date) {
        DateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return simple.format(date);

    }

    public static Date stringToDate(String stringDate) {
        Date date = new Date();
        try {
            date =  new SimpleDateFormat("dd.M.yyyy HH:mm").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
