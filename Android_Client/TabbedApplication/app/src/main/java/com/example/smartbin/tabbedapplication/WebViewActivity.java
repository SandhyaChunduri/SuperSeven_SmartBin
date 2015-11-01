package com.example.smartbin.tabbedapplication;

/**
 * Created by Sandhya Chunduri on 10/30/2015.
 */

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

    private String url = null;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            url = extras.getString("new_variable_name");
        }

        WebView webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);

        if(url != null) {
            webView.loadUrl(url);
        }
        else {
            webView.loadUrl("file:///android_asset/fillTrendByDate.html");
        }

    }

}