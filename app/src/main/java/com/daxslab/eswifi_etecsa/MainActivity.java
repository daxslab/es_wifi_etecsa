package com.daxslab.eswifi_etecsa;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

    public static final String NOTIFICATION_CHANNEL_ID = "ES_WIFI_ETECSA_MAIN_CHANNEL";
    public static final String NOTIFICATION_CHANNEL_NAME = "ES_WIFI_ETECSA_MAIN_CHANNEL";

    private static final int CODE_LOCATION_PERMISSION_REQUEST = 5297;
    WebView webview;
    final static public String ACTIVITY_MESSAGE = "activityMessage";

    public String webUrl = "file:///android_asset/web/index.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webview = findViewById(R.id.webview);

        WebSettings settings = webview.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);

        if (!hasLocationPermission()) {
            runPermissionDialogs();
        }

        startService(this);

    }

    public static void startService(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(context, NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME);
        }
        Intent serviceIntent = new Intent(context, AppForegroundService.class);
        ContextCompat.startForegroundService(context, serviceIntent);

    }


    @RequiresApi(Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String channelId, String channelName) {
        NotificationChannel chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);
    }

    private void requestLocationPermission(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, CODE_LOCATION_PERMISSION_REQUEST);
    }


    private boolean hasLocationPermission(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void runPermissionDialogs(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle(R.string.permission_required);
        alertBuilder.setMessage(R.string.location_permission_required_description);
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int which) {
                requestLocationPermission();
            }});

        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        String message = (String)this.getIntent().getCharSequenceExtra(ACTIVITY_MESSAGE);
        if (message == null){
            message = "";
        }
        webview.loadUrl(webUrl+message);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
}
