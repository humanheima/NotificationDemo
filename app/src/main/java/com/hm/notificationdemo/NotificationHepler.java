package com.hm.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;

/**
 * Created by dumingwei on 2018/4/7 0007.
 */
public class NotificationHepler extends ContextWrapper {

    public static final String PRIMARY_CHANNEL = "default";
    public static final String SECONDARY_CHANNEL = "second";
    public static final String PRIMARY_HANNEL_NAME = "com.hm.notificationdemo.notification_default_channel";
    public static final String SECONDARY_HANNEL_NAME = "com.hm.notificationdemo.notification_secondary_channel";

    private NotificationManager manager;

    public NotificationHepler(Context cxt) {
        super(cxt);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel primaryChannel = new NotificationChannel(PRIMARY_CHANNEL, PRIMARY_HANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            primaryChannel.setLightColor(Color.GREEN);
            primaryChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            getManager().createNotificationChannel(primaryChannel);

            NotificationChannel secondaryChannel = new NotificationChannel(SECONDARY_CHANNEL,
                    SECONDARY_HANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            secondaryChannel.setLightColor(Color.BLUE);
            secondaryChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getManager().createNotificationChannel(secondaryChannel);
        }
    }

    public NotificationCompat.Builder getPrimaryNotification(String title, String body) {
        return new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true);
    }

    public NotificationCompat.Builder getSecondrayNotification(String title, String body) {
        return new NotificationCompat.Builder(getApplicationContext(), SECONDARY_CHANNEL)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true);
    }

    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    private int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    private NotificationManager getManager() {
        if (manager == null) {
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }

}
