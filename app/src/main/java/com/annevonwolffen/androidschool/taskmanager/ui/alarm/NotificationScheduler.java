package com.annevonwolffen.androidschool.taskmanager.ui.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.annevonwolffen.androidschool.taskmanager.ui.contract.IOnTaskOverdueListener;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.dateToString;

public class NotificationScheduler {

    public static final String EXTRA_LABEL = "label";
    public static final String EXTRA_DATETIME = "dateTime";
    public static final String ACTION_REFRESH_ON_OVERDUE = "ActionRefresh";
    public static final int REQ_CODE_REFRESH = 1;

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


    public void setAlarm(String label, Date date) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra(EXTRA_LABEL, label);
        intent.putExtra(EXTRA_DATETIME, dateToString(date));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                (int) date.getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //передаем аларму время пробуждения устройства, время задачи
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);
    }

    public void setOverdueRefresh(Date date, IOnTaskOverdueListener listener) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.setAction(ACTION_REFRESH_ON_OVERDUE);

        AlarmReceiver.bindListener(listener);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
                REQ_CODE_REFRESH, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //передаем аларму время пробуждения устройства, время задачи
        mAlarmManager.set(AlarmManager.RTC_WAKEUP, date.getTime(), pendingIntent);

    }


    /**
     * метод для удаления события (если удаляется событие, то удаляется и нотификация)
     */
    public void removeAlarm (Date date) {
        Intent intent = new Intent(mContext, AlarmReceiver.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) date.getTime(),
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //отменяем оповещение
        mAlarmManager.cancel(pendingIntent);
    }

}
