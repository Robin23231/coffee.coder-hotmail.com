package com.demo.winery.ads;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.adapter.TabAdapter;
import com.demo.winery.ads_my.Tab1_ads_my;
import com.demo.winery.ads_my.Tab2_ads_my;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.material.tabs.TabLayout;

public class Ads_details_my extends AppCompatActivity implements View.OnClickListener{

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String id;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_details_my);

        Methods.closeProgress();
        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1_ads_my(), "Edit My Ads");
        adapter.addFragment(new Tab2_ads_my(), "Print");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public String id_selector(){
        return id;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        }
    }
}