package com.example.asus.androidadvance_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class BrowserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);

        Intent i = getIntent();
        Bundle b =i.getBundleExtra("data");
        String url = b.getString("linkData");

        WebView wv = (WebView)findViewById(R.id.webView);
        wv.loadUrl(url);

    }
}
