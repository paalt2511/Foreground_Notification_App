package com.tones.frnotifications.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.tones.frnotifications.R;

import static com.tones.frnotifications.notification.ForegroundNotificationService.CHANNEL_ID;


public class NotificationView {


    private static final String TAG = NotificationView.class.getSimpleName();
    private Context context;
    RemoteViews views;
    NotificationCompat.Builder customNotificationBuilder;


    public NotificationView(Context context) {

        this.context = context;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public Notification getCallNotification(final Context trackPlayService) {
        Log.d(TAG, "getCallNotification called");
        views = new RemoteViews(trackPlayService.getPackageName(), R.layout.foreground_layout);
        Intent foregroundViewIntent = new Intent(trackPlayService, ForegroundNotificationService.class);
        foregroundViewIntent.setAction(ForegroundNotificationService.Action.FOREGROUND_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground(trackPlayService);
        }
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inMutable = true;
        customNotificationBuilder = new NotificationCompat.Builder(trackPlayService, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setChannelId(CHANNEL_ID)
                .setCustomContentView(views);
        PendingIntent foreGroundPendingIntent = PendingIntent.getService(trackPlayService, 0, foregroundViewIntent, 0);
        views.setOnClickPendingIntent(R.id.forground_layout, foreGroundPendingIntent);
        Notification myForegroundServiceNotification = customNotificationBuilder.build();
        Log.d(TAG, "myForegroundServiceNotification returned successfully");
        return myForegroundServiceNotification;
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(Context context) {
        String channelName = "notification_channel";
        NotificationChannel chan = new NotificationChannel(CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }





}
