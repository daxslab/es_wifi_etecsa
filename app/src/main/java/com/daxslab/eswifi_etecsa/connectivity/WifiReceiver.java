package com.daxslab.eswifi_etecsa.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.daxslab.eswifi_etecsa.R;
import com.daxslab.eswifi_etecsa.utils.MacUtils;
import com.daxslab.eswifi_etecsa.utils.NotificationUtils;
import com.daxslab.eswifi_etecsa.utils.StringTrimmer;
import com.daxslab.eswifi_etecsa.utils.WifiUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * Broadcast receiver listening for wifi connections. Should start wifi checks
 */

public class WifiReceiver extends BroadcastReceiver {

    public static final int SSL_WARNING_NOTIFICATION_ID = 00111;
    public static final int CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID = 00112;
    public static final int NO_OFFICIAL_AP_WARNING_NOTIFICATION_ID = 00113;
    public static final int SIMILAR_WIFI_NAME_WARNING_NOTIFICATION_ID = 00114;
    public static final int TRACEROUTE_WARNING_NOTIFICATION_ID = 00115;
    public static final String WIFI_ETECSA_SSID = "WIFI_ETECSA";
    public static final String CAPTIVE_PORTAL_ADDRESS = "secure.etecsa.net";
    public static final String CAPTIVE_PORTAL_URL = "https://"+CAPTIVE_PORTAL_ADDRESS+":8443/";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (WifiUtils.isReceiverWifiConnected(intent)) {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            checkSimilarWifiName(context);
            if (isWifiEtecsaSSID(context)) {
                new TraceRoutes(context).execute();
                new CheckPortal(context).execute(new String[]{CAPTIVE_PORTAL_URL});
                this.checkApAddress(context);
            }
        }
    }

    /**
     * Check if current wifi connection has WIFI_ETECSA SSID
     * @param context
     * @return true if current wifi connection has WIFI_ETECSA SSID
     */
    private boolean isWifiEtecsaSSID(Context context){
        return Objects.equals(WifiReceiver.WIFI_ETECSA_SSID, WifiUtils.getCurrentSSID(context));
    }

    /**
     * Check if current wifi connection has a WIFI_ETECSA similar SSID and launch a system
     * notification if positive.
     * @param context
     */
    public void checkSimilarWifiName(Context context) {
        String ssid = WifiUtils.getCurrentSSID(context);
        ssid = StringTrimmer.trim(ssid, "_");
        ssid = StringTrimmer.trim(ssid, " ");
        ssid = ssid.toLowerCase();

        String origin = WifiReceiver.WIFI_ETECSA_SSID.toLowerCase();

        JaroWinkler jw = new JaroWinkler();

        if (!isWifiEtecsaSSID(context) && jw.similarity(ssid, origin) >= 0.88){
            NotificationUtils.createNotification(context,context.getString(R.string.app_name), context.getString(R.string.app_name), WifiReceiver.SIMILAR_WIFI_NAME_WARNING_NOTIFICATION_ID, context.getString(R.string.similar_wifi_name_warning), context.getString(R.string.similar_wifi_name));

        }
    }

    /**
     * Check if current wifi connection has a WIFI_ETECSA BSSID (check for Huawei devices) and
     * launch a system notification if not.
     * @param context
     */
    public void checkApAddress(Context context) {
        if (!MacUtils.isHuaweiAddress(WifiUtils.getCurrentBSSID(context))) {
            NotificationUtils.createNotification(context,context.getString(R.string.app_name), context.getString(R.string.app_name), WifiReceiver.NO_OFFICIAL_AP_WARNING_NOTIFICATION_ID, context.getString(R.string.not_official_ap_warning), context.getString(R.string.not_connected_official_ap));
        }
    }

}


