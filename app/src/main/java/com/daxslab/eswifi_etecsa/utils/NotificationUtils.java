package com.daxslab.eswifi_etecsa.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

public class NotificationUtils {


    public static void createNotification(Context mContext, int notificationId, String contentTitle, String contentText) {


        NotificationManager mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        //Build the notification using Notification.Builder
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(android.R.drawable.alert_dark_frame)
                .setAutoCancel(true)
                .setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSound(alarmSound);


        //Show the notification
        mNotificationManager.notify(notificationId, builder.build());
    }

}
