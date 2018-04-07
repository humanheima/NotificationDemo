package com.hm.notificationdemo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hm.notificationdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    public static final int REQUEST_CODE = 0;
    public static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MY_CHANNEL";
    public static final String CHANNEL_NAME = "com.hm.notificationdemo.notification_channel";
    private ActivityMainBinding binding;
    private int numMessages;
    private NotificationCompat.Builder builder;
    private NotificationHepler notificationHepler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        notificationHepler = new NotificationHepler(this);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_send_notification:
                sendNotification();
                break;
            case R.id.btn_launch_first_activity:
                break;
            case R.id.btn_launch_second_activity:
                break;
            default:
                break;
        }

    }

    private void sendNotification() {
        builder = notificationHepler.getPrimaryNotification("My notification", "Hello world");
        Intent newIntent = new Intent(this, ThirdActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        applyExpandedLayout();
        notificationHepler.notify(NOTIFICATION_ID, builder);
    }

    /**
     * 在通知中显示无限进度的progressBar
     */
    private void showProgressIndeterminate() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        builder.setProgress(0, 0, true);
                        notificationHepler.notify(NOTIFICATION_ID, builder);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        builder.setContentText("Download complete")
                                .setProgress(0, 0, false);
                        notificationHepler.notify(NOTIFICATION_ID, builder);
                    }
                }
        ).start();
    }

    /**
     * 在通知中显示有固定进度的progress
     */
    private void showNotificationProgress() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        int incr;
                        for (incr = 0; incr <= 100; incr += 10) {
                            builder.setProgress(100, incr, false);
                            // Displays the progress bar for the first time.
                            notificationHepler.notify(NOTIFICATION_ID, builder);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                Log.d(TAG, "sleep failure");
                            }
                        }
                        // When the loop is finished, updates the notification
                        builder.setContentText("Download complete")
                                // Removes the progress bar
                                .setProgress(0, 0, false);
                        notificationHepler.notify(NOTIFICATION_ID, builder);
                    }
                }
        ).start();
    }

    /**
     * 将扩展布局应用于通知,使通知出现在展开视图中
     */
    private void applyExpandedLayout() {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        for (int i = 0; i < events.length; i++) {
            events[i] = "event i";
        }
        // inboxStyle.setBigContentTitle("Event tracker details:");
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        builder.setStyle(inboxStyle);
    }

}
