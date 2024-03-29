package com.annevonwolffen.androidschool.taskmanager.ui.alarm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.annevonwolffen.androidschool.taskmanager.R;
import com.annevonwolffen.androidschool.taskmanager.ui.contract.IOnTaskOverdueListener;
import com.annevonwolffen.androidschool.taskmanager.ui.view.MainActivity;

import java.util.Date;

import static com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler.EXTRA_DATETIME;
import static com.annevonwolffen.androidschool.taskmanager.ui.alarm.NotificationScheduler.EXTRA_LABEL;
import static com.annevonwolffen.androidschool.taskmanager.ui.util.ConvertUtils.stringToDate;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "Channel_1";

    private static IOnTaskOverdueListener mListener;

    public static void bindListener(IOnTaskOverdueListener listener) {
        mListener = listener;

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            if (mListener != null) {
                mListener.onReboot();
            }
        } else {
            if (mListener != null) {
                mListener.onOverdue();
            }

            if (intent.getStringExtra(EXTRA_LABEL) != null && intent.getStringExtra(EXTRA_DATETIME) != null) {
                String title = intent.getStringExtra(EXTRA_LABEL);
                String dateTime = intent.getStringExtra(EXTRA_DATETIME);


                Intent resultIntent = new Intent(context, MainActivity.class);

                //если приложение закрыто, AlarmReceiver будет стартовать новое активити
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) stringToDate(dateTime).getTime(),
                        resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                createNotificationChannel(context);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

                Notification notification = createNotification(context, title, dateTime, pendingIntent);

                notificationManager.notify((int) stringToDate(dateTime).getTime(), notification);
            }
        }
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID,
                    "Channel_1", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Channel_1_descr");
            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    private Notification createNotification(Context context, String label, String dateTime, PendingIntent pendingIntent) {

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_event_note_black_24dp)
                .setContentTitle(label)
                .setContentText(context.getString(R.string.notification_content_text) + " " + dateTime)
                .setOnlyAlertOnce(true)
                .setSound(alarmSound)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        return builder.build();
    }
}
