package com.demo.winery.setting;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.adapter.TabAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class activity_Setting extends AppCompatActivity implements  View.OnClickListener{

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_setting);


        viewPager = (ViewPager) findViewById(R.id.viewPager_event);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout_event);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment_setting(), "User Info");
        adapter.addFragment(new Tab2Fragment_setting(), "Setting");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

    }
    @Override
    public void onClick(View v) {

    }
}
