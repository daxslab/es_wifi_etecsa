package com.daxslab.eswifi_etecsa.connectivity;

import android.content.Context;
import android.content.Intent;

import com.daxslab.eswifi_etecsa.R;
import com.daxslab.eswifi_etecsa.utils.MacUtils;
import com.daxslab.eswifi_etecsa.utils.NotificationUtils;
import com.daxslab.eswifi_etecsa.utils.StringTrimmer;
import com.daxslab.eswifi_etecsa.utils.WifiUtils;

import java.util.Objects;


import info.debatty.java.stringsimilarity.JaroWinkler;

/**
 * Broadcast receiver listening for wifi connections. Should start wifi checks
 */

public class WifiReceiverActions {

    public static final int SSL_WARNING_NOTIFICATION_ID = 8111;
    public static final int CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID = 8112;
    public static final int NO_OFFICIAL_AP_WARNING_NOTIFICATION_ID = 8113;
    public static final int SIMILAR_WIFI_NAME_WARNING_NOTIFICATION_ID = 8114;
    public static final int TRACEROUTE_WARNING_NOTIFICATION_ID = 8115;
    public static final String WIFI_ETECSA_SSID = "WIFI_ETECSA";
    public static final String CAPTIVE_PORTAL_ADDRESS = "secure.etecsa.net";
    public static final String CAPTIVE_PORTAL_URL = "https://"+CAPTIVE_PORTAL_ADDRESS+":8443/";


    public void onReceive(Context context, Intent intent) {
        if (WifiUtils.isReceiverWifiConnected(intent)) {
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
        return Objects.equals(WifiReceiverActions.WIFI_ETECSA_SSID, WifiUtils.getCurrentSSID(context));
    }

    /**
     * Check if current wifi connection has a SSID similar to "WIFI_ETECSA" using the Jaro-Winkler
     * distance algorithm, and launch a system notification in case it does.
     * @param context
     * @see <a href="https://en.wikipedia.org/wiki/Jaro–Winkler_distance" target="_top">Jaro–Winkler_distance</a>
     */
    public void checkSimilarWifiName(Context context) {
        String ssid = WifiUtils.getCurrentSSID(context);
        ssid = StringTrimmer.trim(ssid, "_");
        ssid = StringTrimmer.trim(ssid, " ");
        ssid = ssid.toLowerCase();

        String origin = WifiReceiverActions.WIFI_ETECSA_SSID.toLowerCase();

        JaroWinkler jw = new JaroWinkler();

        if (!isWifiEtecsaSSID(context) && jw.similarity(ssid, origin) >= 0.88){
            NotificationUtils.createNotification(context,context.getString(R.string.app_name), context.getString(R.string.app_name), WifiReceiverActions.SIMILAR_WIFI_NAME_WARNING_NOTIFICATION_ID, context.getString(R.string.similar_wifi_name_warning), context.getString(R.string.similar_wifi_name),"#alert-1");

        }
    }

    /**
     * Check if current wifi connection has a WIFI_ETECSA BSSID (check for Huawei devices) and
     * launch a system notification if not.
     * @param context
     */
    public void checkApAddress(Context context) {
        if (!MacUtils.isHuaweiAddress(WifiUtils.getCurrentBSSID(context))) {
            NotificationUtils.createNotification(context,context.getString(R.string.app_name), context.getString(R.string.app_name), WifiReceiverActions.NO_OFFICIAL_AP_WARNING_NOTIFICATION_ID, context.getString(R.string.not_official_ap_warning), context.getString(R.string.not_connected_official_ap), "#alert-3");
        }
    }

}


