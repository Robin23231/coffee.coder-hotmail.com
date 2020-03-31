package com.demo.winery.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.winery.R;

public class activity_Link extends AppCompatActivity {

    WebView webView;
    String link;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_link);

        Bundle extras = getIntent().getExtras();

        link = extras.getString("LINK");
        Log.e("link", link);

        webView = (WebView) findViewById(R.id.wb_link);
        webView.loadUrl(link);

//        Intent progressIntent = new Intent(activity_Link.this, stripe_main.class);
//        progressIntent.putExtra("PLAN_FLAG", from);
//        startActivity(progressIntent);
    }
}