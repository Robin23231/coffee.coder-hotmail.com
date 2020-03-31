package com.demo.winery.my_activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.adapter.TabAdapter;
import com.demo.winery.my_fragment.Tab1Fragment_my;
import com.demo.winery.my_fragment.Tab2Fragment_my;
import com.demo.winery.my_fragment.Tab3Fragment_my;
import com.demo.winery.utils.constant;
import com.google.android.material.tabs.TabLayout;

public class activity_Details_my extends AppCompatActivity implements View.OnClickListener{

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

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment_my(), "Edit My Winery");
        adapter.addFragment(new Tab2Fragment_my(), "Review");
        adapter.addFragment(new Tab3Fragment_my(), "Close Account");
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