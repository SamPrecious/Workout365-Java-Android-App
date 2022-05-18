package com.example.workout365;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class UserGuide extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_guide);

        webView = (WebView) findViewById(R.id.userGuide);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/user_guide.html");
    }
}