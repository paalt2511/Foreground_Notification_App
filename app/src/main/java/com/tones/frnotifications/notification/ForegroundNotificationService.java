package com.tones.frnotifications.notification;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.tones.frnotifications.MainActivity;

import static com.tones.frnotifications.notification.ForegroundNotificationService.Action.ACTION_START_FOREGROUND_SERVICE;
import static com.tones.frnotifications.notification.ForegroundNotificationService.Action.ACTION_STOP_FOREGROUND_SERVICE;


public class ForegroundNotificationService extends Service {

    private static final String TAG = ForegroundNotificationService.class.getSimpleName();
    public static final String CHANNEL_ID = "com.tones.frnotifications.channel";
    public static final int FOREGROUND_NOTIFICATION_ID = 1;
    private NotificationView callNotification = null;


    public ForegroundNotificationService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();

            switch (action) {
                case ACTION_START_FOREGROUND_SERVICE:
                    startForegroundService();
                    break;
                case ACTION_STOP_FOREGROUND_SERVICE:
                    stopForegroundService();
                    break;
                case Action.FOREGROUND_VIEW:
                    Intent activityIntent = new Intent(ForegroundNotificationService.this, MainActivity.class);
                    activityIntent.putExtra(Action.FOREGROUND_VIEW, true);
                    activityIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(activityIntent);
                    break;

            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    /* Used to build and start foreground service. */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void startForegroundService() {
        callNotification = new NotificationView(ForegroundNotificationService.this);
        Notification notification = callNotification.getCallNotification(ForegroundNotificationService.this);
        startForeground(FOREGROUND_NOTIFICATION_ID, notification);
    }

    private void stopForegroundService() {
        Log.d(TAG, "Stop foreground service.");
        stopForeground(true);
        stopSelf();
    }


    public static class Action {
        public static final String FOREGROUND_VIEW = "com.rv.db.action.foregroundview";
        public static final String ACTION_START_FOREGROUND_SERVICE = "com.rv.db.startforeground";
        public static final String ACTION_STOP_FOREGROUND_SERVICE = "com.rv.db.stopfroreground";

    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }


}
