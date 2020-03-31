package com.demo.winery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.demo.winery.activities.activity_SignIn;
import com.demo.winery.utils.constant;

public class Activity_Spalsh extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_splash);

        showBasicSplash();
        //clear();
    }
    private void showBasicSplash() {
        new Thread(){
            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message m){
//            if(getBoolean("SaveLogin")){
//                boolean a = getBoolean("SaveLogin");
//                  Intent intent = new Intent(Activity_Spalsh.this, activity_Main.class);
//                  startActivity(intent);
//            }else {
//                Intent intent = new Intent(Activity_Spalsh.this, activity_SignIn.class);
//                startActivity(intent);
//            }
                Intent intent = new Intent(Activity_Spalsh.this, activity_SignIn.class);
                startActivity(intent);
            finish();
        }
    };

    public synchronized boolean getBoolean(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        Boolean  selected =  mSharedPreferences.getBoolean(key, false);
        return selected;
    }
    public synchronized void clear() {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.clear();
        mEditor.apply();
    }
}
