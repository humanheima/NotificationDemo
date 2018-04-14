package com.hm.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import java.util.Arrays;

/**
 * Created by dumingwei on 2018/4/7 0007.
 */
public class NotificationHelper extends ContextWrapper {

    public static final String PRIMARY_CHANNEL = "default";
    public static final String SECONDARY_CHANNEL = "second";
    public static final String PRIMARY_HANNEL_NAME = "com.hm.notificationdemo.notification_default_channel";
    public static final String SECONDARY_HANNEL_NAME = "com.hm.notificationdemo.notification_secondary_channel";
    private final String TAG = getClass().getSimpleName();
    private NotificationManager manager;

    public NotificationHelper(Context cxt) {
        super(cxt);
        getManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel primaryChannel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_HANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            primaryChannel.enableLights(true);
            primaryChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationChannel secondaryChannel = new NotificationChannel(SECONDARY_CHANNEL,
                    SECONDARY_HANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            secondaryChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            getManager().createNotificationChannels(Arrays.asList(primaryChannel, secondaryChannel));
        }
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

    public NotificationCompat.Builder getPrimaryNotification(String title, String text) {
        return new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL)
                .setContentTitle(title)
                .setContentText(text)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_MAX)//设置最高优先级
                .setAutoCancel(true);
    }

    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    public NotificationCompat.Builder getSecondrayNotification(String title, String text) {
        return new NotificationCompat.Builder(getApplicationContext(), SECONDARY_CHANNEL)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_notify_chat))
                .setAutoCancel(true);
    }

    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

}
