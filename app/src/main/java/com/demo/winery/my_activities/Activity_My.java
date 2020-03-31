package com.demo.winery.my_activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.demo.winery.R;
import com.demo.winery.activities.activity_Main;
import com.demo.winery.ads_my.myAdsFragment;
import com.demo.winery.my_fragment.myEventFragment;
import com.demo.winery.my_fragment.myFestivalFragment;
import com.demo.winery.my_fragment.myTourFragment;
import com.demo.winery.my_fragment.myWineryFragment;
import com.demo.winery.setting.activity_Setting;
import com.demo.winery.subscription.master_subscription;
import com.demo.winery.utils.constant;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

public class Activity_My extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Fragment fragment;
    String Email;
    Bundle extras;
    Bundle bundle = new Bundle();
    TextView tv_home, tv_setting;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_my);

        fragment = new myWineryFragment();
        loadFragment(fragment);

        Email = getIntent().getStringExtra("MYEMAIL");
        tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);
        tv_setting = findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_my);

        if(!getBoolean(constant.is_admin)){
            navigation.getMenu().removeItem(R.id.navigation_paymod);
        }
        navigation.setOnNavigationItemSelectedListener(this);
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_home:
                Intent intent_back = new Intent(Activity_My.this, activity_Main.class);
                startActivity(intent_back);
                break;
            case R.id.tv_setting:
                Intent setting_intent = new Intent(Activity_My.this, activity_Setting.class);
                startActivity(setting_intent);
                break;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {


        fragment = null;
        switch (item.getItemId()){
            case R.id.navigation_winery:
                fragment = new myWineryFragment();
                bundle.putString("ToMYWineryEmail", Email);
                fragment.setArguments(bundle);
                break;
            case R.id.navigation_event:
                fragment = new myEventFragment();
                break;
            case R.id.navigation_tour:
                fragment = new myTourFragment();
                bundle.putString("ToMYWineryEmail", Email);
                fragment.setArguments(bundle);
                break;
            case R.id.navigation_festival:
                fragment = new myFestivalFragment();
                break;
            case R.id.navigation_ads:
                fragment = new myAdsFragment();
                break;
            case R.id.navigation_paymod:
                fragment = new master_subscription();
                break;
        }
        return loadFragment(fragment);
    }
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container_my, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    public void onBackPressed() {

    }
    public synchronized boolean getBoolean(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        Boolean  selected =  mSharedPreferences.getBoolean(key, false);
        return selected;
    }
}