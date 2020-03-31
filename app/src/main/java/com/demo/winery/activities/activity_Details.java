package com.demo.winery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.demo.winery.R;
import com.demo.winery.adapter.TabAdapter;
import com.demo.winery.fragment.GoogleMapFragment;
import com.demo.winery.fragment.Tab1Fragment;
import com.demo.winery.fragment.Tab2Fragment;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.tabs.TabLayout;

public class activity_Details extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    private TabAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    String id;
    Button btn_winery_claim;
    //MapView winery_mapView;
    String address, zip, distance;
    MapView mapView;
    GoogleMap map;

    float star_review;
    Button map_btn;
    Intent Askpay_intent;
    //RatingBar map_view_rate;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_details);

        Methods.closeProgress();

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        address = data.getStringExtra(constant.ADDRESS);
        zip = data.getStringExtra(constant.ZIP);
        distance = data.getStringExtra(constant.DISTANCE);

        Log.e("detail_id", id);

        star_review = data.getFloatExtra(constant.REVIEW_STAR,0.0f);
        btn_winery_claim = findViewById(R.id.btn_winery_claim);
        btn_winery_claim.setOnClickListener(this);

        ///winery_mapView = (MapView) findViewById(R.id.winery_mapView);
        //initGoogleMap(savedInstanceState);

        //map_view_rate = findViewById(R.id.map_view_rate);
        //map_view_rate.setRating(star_review);

        mapView = (MapView) findViewById(R.id.winery_mapView);
        mapView.onCreate(savedInstanceState);


        mapView.getMapAsync(this);

        map_btn = (Button) findViewById(R.id.map_btn);
        map_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new GoogleMapFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.google_container, fragment, fragment.getClass().getSimpleName()).addToBackStack(null).commit();
            }
        });





        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        adapter = new TabAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1Fragment(), "About");
        adapter.addFragment(new Tab2Fragment(), "Review");
        //adapter.addFragment(new Tab3Fragment(), "Wine");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

//    private void initGoogleMap(Bundle savedInstanceState){
//        Bundle mapViewBundle = null;
//        if (savedInstanceState != null) {
//            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
//        }
//        winery_mapView.onCreate(mapViewBundle);
//        winery_mapView.getMapAsync(this);
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_winery_claim:
                Askpay_intent = new Intent(activity_Details.this, activity_AskPay.class);
                Askpay_intent.putExtra(constant.PAYMONEY, "winery");
                startActivity(Askpay_intent);
                break;
        }

    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//
//        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
//        if (mapViewBundle == null) {
//            mapViewBundle = new Bundle();
//            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
//        }
//        winery_mapView.onSaveInstanceState(mapViewBundle);
//    }

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

//    @Override
//    public void onMapReady(GoogleMap map) {
//
//        int height = 150;
//        int width = 100;
//        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
//        Bitmap b = bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//
//        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        LatLng address_location = getLocationFromAddress(activity_Details.this, zip);
//        map.addMarker(new MarkerOptions().position(address_location).title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
//        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
//        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
//    }

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

    //how to get Latitude and Longtitude from address
//    public LatLng getLocationFromAddress(Context context, String strAddress)
//    {
//        Geocoder coder= new Geocoder(context);
//        List<Address> addresses;
//        LatLng p1 = null;
//
//        try
//        {
//            addresses = coder.getFromLocationName(strAddress, 5);
//            if(addresses==null)
//            {
//                return null;
//            }
//            Address location = addresses.get(0);
//            location.getLatitude();
//            location.getLongitude();
//
//            p1 = new LatLng(location.getLatitude(), location.getLongitude());
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//        return p1;
//
//    }

    public String id_selector(){
        return id;
    }
    public String get_zip(){
        return zip;
    }
    public String get_distance(){
        return distance;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
       /*
       //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
       */

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(43.1, -87.9), 10);
        map.animateCamera(cameraUpdate);
        map.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(43.1, -87.9)));

    }
}