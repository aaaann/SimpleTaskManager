package com.annevonwolffen.androidschool.taskmanager.ui.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.annevonwolffen.androidschool.taskmanager.data.model.Task;

import java.util.Date;
import java.util.List;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.substractDays;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.substractHours;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.substractMinutes;

public class NotificationScheduler {

    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_DATETIME = "dateTime";

    private static NotificationScheduler instance;
    private Context mContext;
    private AlarmManager mAlarmManager;

    public static NotificationScheduler getInstance() {
        if (instance == null) {
            instance = new NotificationScheduler();
        }
        return instance;
    }


    public void init (Context context) {
        mContext = context;
        mAlarmManager = (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
    }


    public void setAlarm(String label, Date date, boolean turnOnNotification) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        if (turnOnNotification) {
            intent.putExtra(EXTRA_LABEL, label);
            intent.putExtra(EXTRA_DATETIME, dateToString(date));
        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                (int) date.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //передаем аларму время пробуждения устройства, время задачи
        // создать по возможности 4 нотификации согласно настройкам (по умолчанию за день, за час, за 10 мин, вовремя)
        // todo: parameters take from sharedPreferences
        Date notification1Date =  substractDays(date, 1);// за день
        Date notification2Date = substractHours(date, 1); // за час
        Date notification3Date = substractMinutes(date, 10); // за 10 мин
        if (new Date().before(notification1Date)) {
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(mContext,
                    (int) notification1Date.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, notification1Date.getTime(), pendingIntent1);
        }
        if (new Date().before(notification2Date)) {
            PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mContext,
                    (int) notification2Date.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, notification2Date.getTime(), pendingIntent2);
        }
        if (new Date().before(notification3Date)) {
            PendingIntent pendingIntent3 = PendingIntent.getBroadcast(mContext,
                    (int) notification3Date.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mAlarmManager.set(AlarmManager.RTC_WAKEUP, notification3Date.getTime(), pendingIntent3);
        }
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }

    public void setAlarm(List<Task> tasks) {
        for (Task task : tasks) {
            setAlarm(task.getTitle(), task.getDateTo(), task.isNotifAdded());
        }
    }

    /**
     * метод для удаления события (если удаляется событие, то удаляется и нотификация)
     */
    public void removeAlarm (Date date) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) date.getTime(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(mContext, (int) substractDays(date, 1).getTime(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(mContext, (int) substractHours(date, 1).getTime(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(mContext, (int) substractMinutes(date, 10).getTime(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //отменяем оповещение
        mAlarmManager.cancel(pendingIntent1);
        mAlarmManager.cancel(pendingIntent2);
        mAlarmManager.cancel(pendingIntent3);
        mAlarmManager.cancel(pendingIntent);
    }

}
