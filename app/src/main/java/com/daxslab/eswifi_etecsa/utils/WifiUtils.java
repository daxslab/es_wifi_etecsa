package com.daxslab.eswifi_etecsa.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiUtils {

    /**
     * @param intent context Intent
     * @return true if wifi is connected
     */
    public static boolean isReceiverWifiConnected(Intent intent){
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        Log.d("akak", "isReceiverWifiConnected: "+info.toString());
        return  info != null && info.isConnected();
    }

    /**
     * @param context app context
     * @return true if wifi is enabled
     */
    public static boolean isWifiEnabled(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
                Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * @param cm Connectivity manager
     * @return true if wifi is enabled
     */
    public static boolean isWifiEnabled(ConnectivityManager cm){
        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (wifiNetwork != null && wifiNetwork.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * @param context
     * @return BSSID of current connected AP
     */
    public static String getCurrentBSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

    /**
     * @param context
     * @return SSID of current connected AP
     */
    public static String getCurrentSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        ssid = ssid.startsWith("\"") ? ssid.substring(1) : ssid;
        ssid = ssid.endsWith("\"") ? ssid.substring(0, ssid.length()-1) : ssid;
        return ssid;
    }

}
