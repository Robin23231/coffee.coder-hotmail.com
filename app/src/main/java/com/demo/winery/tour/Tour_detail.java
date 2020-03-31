package com.demo.winery.tour;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.activities.activity_Link;
import com.demo.winery.adapter.TabAdapter;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

import static com.demo.winery.utils.constant.MAPVIEW_BUNDLE_KEY;

public class Tour_detail extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    MapView mapView;
    String id, zip;
    float rating_stars;
    RatingBar map_view_rate;
    Button btn_winery_claim;
    Intent Askpay_intent;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Methods.closeProgress();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_tour_detail);

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        zip = data.getStringExtra(constant.ZIP);
        rating_stars = data.getFloatExtra(constant.REVIEW_STAR, 0.0f);
        //tour_info_progress(id);

        btn_winery_claim = (Button) findViewById(R.id.btn_winery_claim);
        btn_winery_claim.setOnClickListener(this);


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new tour_Tab1_show(), "About");
        adapter.addFragment(new tour_Tab2_review(), "Review");
        //adapter.addFragment(new Tab3Fragment(), "Wine");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);



        map_view_rate = (RatingBar) findViewById(R.id.map_view_rate);
        map_view_rate.setRating(rating_stars);

        mapView = (MapView) findViewById(R.id.mapView);
        initGoogleMap(savedInstanceState);
    }


    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_winery_claim:
//                Askpay_intent = new Intent(Tour_detail.this, activity_AskPay.class);
//                Askpay_intent.putExtra(constant.PAYMONEY, "tour");
//                startActivity(Askpay_intent);

            Intent progressIntent = new Intent(Tour_detail.this, activity_Link.class);
            progressIntent.putExtra("LINK", "https://wineryfinder.org/signups/tour");
            startActivity(progressIntent);
                break;
        }
    }




    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    public void onMapReady(GoogleMap map) {

        int height = 150;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.tour_logo);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng address_location = getLocationFromAddress(Tour_detail.this, zip);
        map.addMarker(new MarkerOptions().position(address_location).title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    public LatLng getLocationFromAddress(Context context, String strAddress)
    {
        Geocoder coder= new Geocoder(context);
        List<Address> addresses;
        LatLng p1 = null;

        try
        {
            addresses = coder.getFromLocationName(strAddress, 5);
            if(addresses==null)
            {
                return null;
            }
            Address location = addresses.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;

    }
    public String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    public String id_selector(){
        return id;
    }
}