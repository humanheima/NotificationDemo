package com.hm.notificationdemo;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RemoteViews;

import com.hm.notificationdemo.databinding.ActivityMainBinding;

import static com.hm.notificationdemo.NotificationHelper.PRIMARY_CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 0;
    public static final int PRIMARY_NOTIFICATION_ID = 1;
    public static final int SECONDARY_NOTIFICATION_ID = 2;

    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private int numMessages;
    private NotificationCompat.Builder builder;
    private NotificationHelper notificationHelper;

    private Notification notification;
    private RemoteViews notificationLayout;

    private int updateProgressWhat = 100;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == updateProgressWhat) {
                int progress = msg.arg1;
                Log.d(TAG, "handleMessage: " + progress);
                notificationLayout.setProgressBar(R.id.progressBar, 200, progress, false);

                builder.setCustomContentView(notificationLayout);
                notification = builder.build();
                notificationHelper.notify(PRIMARY_NOTIFICATION_ID, notification);

                if (progress < 200) {
                    Message message = handler.obtainMessage(updateProgressWhat);
                    message.arg1 = ++progress;
                    handler.sendMessageDelayed(message, 100);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        notificationHelper = new NotificationHelper(this);
        notificationLayout = new RemoteViews(getPackageName(), R.layout.notify_content_view_2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        notificationHelper = null;
        handler.removeCallbacksAndMessages(null);
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.btn_send_primary_notification:
                sendNotification();
                break;
            case R.id.btnCreateExpandedNotify:
                createExpandedNotify();
                break;
            case R.id.btnCustomizeNotify:
                createCustomizeNotify();
                break;
            case R.id.btn_send_secondary_notification:
                sendSecondaryNotification();
                break;
            case R.id.btn_launch_first_activity:
                break;
            case R.id.btn_launch_second_activity:
                break;
            case R.id.btn_open_notification_setting:
                openNotificationSetting();
                break;
            case R.id.btn_open_notification_channel_setting:
                openNotificationChannelSetting(PRIMARY_CHANNEL_ID);
                break;
            default:
                break;
        }
    }

    private void createCustomizeNotify() {
        /**
         * 方式一
         */
        /*builder = notificationHelper.getPrimaryNotification("展开式通知",
                getString(R.string.short_notification_text));
        builder.setCustomContentView(notificationLayout);
        builder.build();
        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);*/

        /**
         * 方式2，就是比上边哪种方式少设置了几个属性
         */
        builder = new NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(android.R.drawable.stat_notify_chat);
        builder.setCustomContentView(notificationLayout);
        notification = builder.build();

        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, notification);

        Message message = handler.obtainMessage(updateProgressWhat);

        //点击通知以后，延迟2秒发送第一个message
        handler.sendMessageDelayed(message, 2000);


    }

    private void createExpandedNotify() {
        builder = notificationHelper.getPrimaryNotification("使用媒体控件创建通知",
                getString(R.string.short_notification_text));
        Intent newIntent = new Intent(this, ThirdActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        builder.setLargeIcon(largeIcon);
        builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(largeIcon).bigLargeIcon(null));

        builder.build();
        //applyExpandedLayout();
        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
    }

    private void sendNotification() {
        builder = notificationHelper.getPrimaryNotification("My notification",
                getString(R.string.short_notification_text));
        builder.setNumber(2);
        Intent newIntent = new Intent(this, ThirdActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        applyExpandedLayout();
        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
    }

    private void sendSecondaryNotification() {
        builder = notificationHelper.getSecondaryNotification("My notification",
                getString(R.string.short_notification_text));
        if (null != builder) {
            Intent newIntent = new Intent(this, SecondActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            notificationHelper.notify(SECONDARY_NOTIFICATION_ID, builder);
        }
    }

    public void openNotificationSetting() {
        notificationHelper.openNotificationSetting();
    }

    public void openNotificationChannelSetting(String channelId) {
        notificationHelper.openChannelSetting(channelId);
    }

    /**
     * 将扩展布局应用于通知,使通知出现在展开视图中
     */
    private void applyExpandedLayout() {
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = new String[6];
        for (int i = 0; i < events.length; i++) {
            events[i] = i + ".event";
        }
        // inboxStyle.setBigContentTitle("Event tracker details:");
        for (int i = 0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }
        inboxStyle.setSummaryText("In box style");
        inboxStyle.setBigContentTitle("In box content title");//如果不设置的话，默认就是Builder.setContentTitle传入的值
        builder.setStyle(inboxStyle);
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
                        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        builder.setContentText("Download complete")
                                .setProgress(0, 0, false);
                        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
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
                            notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
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
                        notificationHelper.notify(PRIMARY_NOTIFICATION_ID, builder);
                    }
                }
        ).start();
    }


}
