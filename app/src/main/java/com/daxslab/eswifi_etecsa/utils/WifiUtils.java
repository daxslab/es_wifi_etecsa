package com.daxslab.eswifi_etecsa.utils;

import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class WifiUtils {

    public static boolean isReceiverWifiConnected(Intent intent){
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        return  info != null && info.isConnected();
    }

    public static String getCurrentBSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getBSSID();
    }

    public static String getCurrentSSID(Context context){
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ssid = wifiInfo.getSSID();
        ssid = ssid.startsWith("\"") ? ssid.substring(1) : ssid;
        ssid = ssid.endsWith("\"") ? ssid.substring(0, ssid.length()-1) : ssid;
        return ssid;
    }

}
