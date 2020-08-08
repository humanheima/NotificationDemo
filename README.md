## Notification
1.  如果通知的text很长的话，可以如下设置
```java
NotificationCompat.Builder(getApplicationContext(), PRIMARY_CHANNEL)
                .setContentTitle(title)
                //不需要设置text
                //.setContentText(text)
                //设置BigTextStyle
                setStyle(new NotificationCompat.BigTextStyle().bigText(getString(R.string.long_notification_text)));
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(getSmallIcon())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round))
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setAutoCancel(true);
```
2. 如果想在通知下面显示一张图片,可如下设置
```java
builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_notification_big)));
```

### 自定义通知布局不能使用约束布局

使用约束布局通知显示不出来。

* [Android自定义通知不显示的问题](https://blog.csdn.net/asssssdaaa/article/details/90271388)

### 让通知一直在状态栏里面不消失

调用NotificationCompat.Builder的setOngoing方法，传入参数为true即可。

```
public Builder setOngoing(boolean ongoing) {
    setFlag(Notification.FLAG_ONGOING_EVENT, ongoing);
    return this;
}
```

### RemoteViews只支持部分布局和控件

```
android.widget.AdapterViewFlipper
android.widget.FrameLayout
android.widget.GridLayout
android.widget.GridView
android.widget.LinearLayout
android.widget.ListView
android.widget.RelativeLayout
android.widget.StackView
android.widget.ViewFlipper

android.widget.AnalogClock
android.widget.Button
android.widget.Chronometer
android.widget.ImageButton
android.widget.ImageView
android.widget.ProgressBar
android.widget.TextClock
android.widget.TextView

```

