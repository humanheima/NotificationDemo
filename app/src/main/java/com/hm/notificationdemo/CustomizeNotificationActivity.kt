package com.hm.notificationdemo

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import android.widget.RemoteViews
import kotlinx.android.synthetic.main.activity_customize_notification.*

/**
 * Created by dumingwei on 2020/8/8
 *
 * Desc: 测试自定义通知布局
 */
class CustomizeNotificationActivity : AppCompatActivity() {

    private lateinit var notificationHelper: NotificationHelper
    private lateinit var builder: NotificationCompat.Builder
    private lateinit var notification: Notification

    private lateinit var contentView: RemoteViews
    private lateinit var bigContentView: RemoteViews

    companion object {

        @JvmStatic
        fun launch(context: Context) {
            val intent = Intent(context, CustomizeNotificationActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customize_notification)

        notificationHelper = NotificationHelper(this)

        contentView = RemoteViews(packageName, R.layout.notify_content_view_normal)
        bigContentView = RemoteViews(packageName, R.layout.notify_content_view_big)


        builder = NotificationCompat.Builder(applicationContext, NotificationHelper.PRIMARY_CHANNEL_ID)
                .setWhen(System.currentTimeMillis())
                .setOngoing(true)
                .setSmallIcon(android.R.drawable.stat_notify_chat)
                .setCustomContentView(contentView)
                .setCustomBigContentView(bigContentView)

        notification = builder.build()

        btnSendNotification.setOnClickListener {
            notificationHelper.notify(MainActivity.PRIMARY_NOTIFICATION_ID, notification)
        }
    }
}
