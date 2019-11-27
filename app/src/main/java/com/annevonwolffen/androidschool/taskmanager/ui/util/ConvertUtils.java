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
            date = new SimpleDateFormat("dd.M.yyyy HH:mm").parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static int[] intDateFromStringDate(String dateTime) {
        String date = dateTime.substring(0, dateTime.indexOf(" "));
        String[] aDate = date.split(".");
        return new int[]{Integer.parseInt(aDate[0]), Integer.parseInt(aDate[1]), Integer.parseInt(aDate[2])};
    }

    public static int[] intTimeFromStringDate(String dateTime) {
        String time = dateTime.substring(dateTime.indexOf(" ") + 1);
        String[] aTime = time.split(":");
        return new int[]{Integer.parseInt(aTime[0]), Integer.parseInt(aTime[0])};
    }
}
