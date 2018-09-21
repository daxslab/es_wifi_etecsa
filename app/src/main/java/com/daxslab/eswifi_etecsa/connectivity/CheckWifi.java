package com.daxslab.eswifi_etecsa.connectivity;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.daxslab.eswifi_etecsa.utils.MacUtils;
import com.daxslab.eswifi_etecsa.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLHandshakeException;

/**
 * Created by cccaballero on 20/09/18.
 */
class CheckWifi extends AsyncTask<String, Void, Boolean> {


    private Context mContext;
    private boolean isWifiOk;

    CheckWifi(Context mContext){
        super();
        this.mContext = mContext;
        this.isWifiOk = true;

    }

    protected Boolean doInBackground(String... params) {

        WifiManager wifiManager = (WifiManager) mContext.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();

        String ssid = wifiInfo.getSSID();
        String bssid = wifiInfo.getBSSID();

        if (Objects.equals(WifiReceiver.WIFI_ETECSA_SSID, ssid)) {
            boolean isConnectionOk =true;
            try {
                isConnectionOk = checkConnection(params[0]);
            }catch (SSLHandshakeException ex){
                createNotification(WifiReceiver.SSL_WARNING_NOTIFICATION_ID, mContext.getString(R.string.ssl_warning), mContext.getString(R.string.not_valid_ssl));
                this.isWifiOk = false;
            }catch (Exception ex){
                createNotification(WifiReceiver.CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID, mContext.getString(R.string.cant_verify_ssl_warning), mContext.getString(R.string.cant_verify_ssl));
                this.isWifiOk = false;
            }

            if (!MacUtils.isHuaweiAddress(bssid)) {
                createNotification(WifiReceiver.NO_OFFICIAL_AP__WARNING_NOTIFICATION_ID, mContext.getString(R.string.not_official_ap_warning), mContext.getString(R.string.not_connected_official_ap));
                this.isWifiOk = false;
            }
            return isConnectionOk;
        }
        return null;
    }

    private boolean checkConnection(String checkUrl) throws InterruptedException, IOException {
        HttpURLConnection response = null;
            TimeUnit.SECONDS.sleep(3);
            URL url = new URL(checkUrl);
            response = (HttpURLConnection) url.openConnection();
            response.setRequestMethod("GET");
            response.connect();
            checkResponse(response);
            return true;
    }

    private void checkResponse(HttpURLConnection response) throws IOException {
        int statusCode = response.getResponseCode();
        if (HttpURLConnection.HTTP_OK != statusCode) {
            throw new IOException("HttpStatus: " + statusCode);
        }
    }

    private void createNotification(int notificationId, String contentTitle, String contentText) {


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
