package com.daxslab.eswifi_etecsa.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.daxslab.eswifi_etecsa.MainActivity;

public class NotificationUtils {


    /**
     * Launch an Android notification
     * @param mContext {@link Context} for launching notification
     * @param notificationId
     * @param contentTitle Notification title
     * @param contentText Notification id
     */
    public static void createNotification(Context mContext, String channelId, String channelTitle, int notificationId, String contentTitle, String contentText) {
        Intent intent;
        NotificationCompat.Builder builder;
        NotificationManager notifManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = notifManager.getNotificationChannel(channelId);
            if (mChannel == null) {
                mChannel = new NotificationChannel(channelId, channelTitle, importance);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                notifManager.createNotificationChannel(mChannel);
            }
            builder = getBuilder(mContext, channelId, contentTitle, contentText);
        }
        else {
            builder = getBuilder(mContext, channelId, contentTitle, contentText);
        }
        Notification notification = builder.build();
        notifManager.notify(notificationId, notification);
    }

    private static NotificationCompat.Builder getBuilder(Context mContext, String channelId, CharSequence contentTitle, CharSequence contentText){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId);
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, 0);
        builder.setContentTitle(contentTitle)
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setContentText(contentText)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setTicker(contentTitle)
                .setVibrate(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

        return builder;

    }

}
