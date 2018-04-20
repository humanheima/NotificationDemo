package com.hm.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.util.Arrays;

/**
 * Created by dumingwei on 2018/4/7 0007.
 */
public class NotificationHelper extends ContextWrapper {

    public static final String PRIMARY_CHANNEL_ID = "default";
    public static final String SECONDARY_CHANNEL_ID = "second";
    public static final String PRIMARY_HANNEL_NAME = "com.hm.notificationdemo.notification_default_channel";
    public static final String SECONDARY_HANNEL_NAME = "com.hm.notificationdemo.notification_secondary_channel";
    private final String TAG = getClass().getSimpleName();
    private NotificationManager manager;

    public NotificationHelper(Context cxt) {
        super(cxt);
        getManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel primaryChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, PRIMARY_HANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);
            primaryChannel.enableLights(true);
            primaryChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            NotificationChannel secondaryChannel = new NotificationChannel(SECONDARY_CHANNEL_ID,
                    SECONDARY_HANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
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
        return new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
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

    public NotificationCompat.Builder getSecondaryNotification(String title, String text) {
        if (notificationChannelEnabled(SECONDARY_CHANNEL_ID)) {
            return new NotificationCompat.Builder(getApplicationContext(), SECONDARY_CHANNEL_ID)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setSmallIcon(getSmallIcon())
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), android.R.drawable.stat_notify_chat))
                    .setAutoCancel(true);
        } else {
            return null;
        }
    }

    private boolean notificationChannelEnabled(String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = getManager().getNotificationChannel(SECONDARY_CHANNEL_ID);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Toast.makeText(getApplicationContext(), "Please enable notification!", Toast.LENGTH_SHORT).show();
                openChannelSetting(channelId);
                return false;
            }
            return true;
        }
        return true;
    }

    public void openChannelSetting(String channelId) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channelId);
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
                startActivity(intent);
        }
    }

    public void notify(int id, NotificationCompat.Builder notification) {
        getManager().notify(id, notification.build());
    }

    public void openNotificationSetting() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null)
                startActivity(intent);
        }
    }


}
