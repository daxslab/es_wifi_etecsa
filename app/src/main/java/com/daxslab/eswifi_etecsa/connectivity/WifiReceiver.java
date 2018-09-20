package com.daxslab.eswifi_etecsa.connectivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

/**
 * Created by cccaballero on 12/08/18.
 */

public class WifiReceiver extends BroadcastReceiver {

    public static int SSL_WARNING_NOTIFICATION_ID = 00111;
    public static int CANT_VERIFY_SSL_WARNING_NOTIFICATION_ID = 00112;
    public static int NO_OFFICIAL_AP__WARNING_NOTIFICATION_ID = 00113;
    public static String WIFI_ETECSA_SSID = "WIFI_ETECSA";

    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if(info != null && info.isConnected() ) {
            new CheckWifi(context).execute(new String[]{"https://test.nauta.cu/"});
        }
    }

}


