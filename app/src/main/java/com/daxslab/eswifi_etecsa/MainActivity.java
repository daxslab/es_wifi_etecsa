package com.daxslab.eswifi_etecsa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class MainActivity extends AppCompatActivity {

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
