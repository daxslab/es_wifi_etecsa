package com.daxslab.eswifi_etecsa.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.daxslab.eswifi_etecsa.R;
import com.daxslab.eswifi_etecsa.utils.MacUtils;
import com.daxslab.eswifi_etecsa.utils.NotificationUtils;
import com.daxslab.eswifi_etecsa.utils.WifiUtils;

import java.util.Objects;

/**
 * Created by cccaballero on 12/08/18.
 */

public class WifiReceiver extends BroadcastReceiver {

    public static int SSL_WARNING_NOTIFICATION_ID = 00111;
    public static int CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID = 00112;
    public static int NO_OFFICIAL_AP__WARNING_NOTIFICATION_ID = 00113;
    public static String WIFI_ETECSA_SSID = "\"WIFI_ETECSA\"";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiUtils.isReceiverWifiConnected(intent)) {

            if (Objects.equals(WifiReceiver.WIFI_ETECSA_SSID, WifiUtils.getCurrentSSID(context))) {
                new CheckPortal(context).execute(new String[]{"https://secure.etecsa.net:8443/"});
                this.checkApAddress(context);
            }
        }
    }

    public void checkApAddress(Context contect) {
        if (!MacUtils.isHuaweiAddress(WifiUtils.getCurrentBSSID(contect))) {
            NotificationUtils.createNotification(contect, WifiReceiver.NO_OFFICIAL_AP__WARNING_NOTIFICATION_ID, contect.getString(R.string.not_official_ap_warning), contect.getString(R.string.not_connected_official_ap));
        }
    }

}


