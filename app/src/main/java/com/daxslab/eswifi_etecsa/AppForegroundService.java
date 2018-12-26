package com.daxslab.eswifi_etecsa;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.daxslab.eswifi_etecsa.connectivity.WifiReceiverActions;

public class AppForegroundService extends Service implements WifiConnectionReceiver.ConnectivityReceiverListener {

    private static final String THREAD_NAME = "MAIN_SERVICE_THREAD";
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private WifiConnectionReceiver mConnectivityReceiver;


    public AppForegroundService() {
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {

            try {
                while (true){
                    try {
                        Thread.sleep(50000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                    e.printStackTrace();
                }

            stopSelf(msg.arg1);
        }
    }

    @Override
    public void onCreate() {

        mConnectivityReceiver = new WifiConnectionReceiver(this);

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(mConnectivityReceiver, intentFilter);

        HandlerThread thread = new HandlerThread(THREAD_NAME,
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);


        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, notificationIntent, 0);

        Notification notification = new NotificationCompat.Builder(this, MainActivity.NOTIFICATION_CHANNEL_ID)
                .setContentTitle(getResources().getString(R.string.service_notification_title))
                .setContentText(getResources().getString(R.string.service_notification_description))
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);

        return START_STICKY;

    }

    @Override
    public void onNetworkConnectionChanged(Context context, Intent intent, boolean isConnected) {
        if (isConnected){
            WifiReceiverActions receiverActions = new WifiReceiverActions();
            receiverActions.onReceive(context, intent);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mConnectivityReceiver);
    }

}
