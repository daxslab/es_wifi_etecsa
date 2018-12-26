package com.daxslab.eswifi_etecsa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.daxslab.eswifi_etecsa.utils.WifiUtils;

public class WifiConnectionReceiver extends BroadcastReceiver {

    private ConnectivityReceiverListener mConnectivityReceiverListener;

    WifiConnectionReceiver(ConnectivityReceiverListener listener) {
        mConnectivityReceiverListener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        mConnectivityReceiverListener.onNetworkConnectionChanged(context, intent, isConnected(context));
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        boolean isWifiConnection = WifiUtils.isWifiEnabled(cm);

        return activeNetwork != null && isWifiConnection && activeNetwork.isConnectedOrConnecting();
    }

    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(Context context, Intent intent, boolean isConnected);
    }
}
