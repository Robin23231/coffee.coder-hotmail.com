package com.demo.winery.activities;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.event.Claim_event;
import com.demo.winery.fragment.AdsFragment;
import com.demo.winery.fragment.EventFragment;
import com.demo.winery.fragment.FestivalFragment;
import com.demo.winery.fragment.TourFragment;
import com.demo.winery.fragment.WineryFragment;
import com.demo.winery.model.ResponseModel;
import com.demo.winery.my_activities.Activity_Before_My;
import com.demo.winery.setting.activity_Setting;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.MySingleton;
import com.demo.winery.utils.constant;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class activity_Main extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener, LocationListener {

    Fragment fragment;
    FloatingActionButton fab_My;
    ResponseModel responseModel;
    Intent Askpay_intent;
    TextView tv_close, tv_setting;
    //LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;
    double current_lat = 0, currend_long = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        Objects.requireNonNull(getSupportActionBar()).hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_main);

        fab_My = findViewById(R.id.fab_My);
        fab_My.setOnClickListener(this);
//        fab_Festival = findViewById(R.id.fab_Festival);
//        fab_Festival.setOnClickListener(this);
        //fab_Tour = findViewById(R.id.fab_Tour);
        //fab_Tour.setOnClickListener(this);
        //fab_Event = findViewById(R.id.fab_Event);
        //fab_Event.setOnClickListener(this);
        //fab_Winery = findViewById(R.id.fab_Winery);
        //fab_Winery.setOnClickListener(this);
//        fab_ADs = findViewById(R.id.fab_ADs);
//        fab_ADs.setOnClickListener(this);

        //fab_Winery.setVisibility(View.GONE);
        //fab_Event.setVisibility(View.GONE);
        //fab_Tour.setVisibility(View.GONE);
        //fab_Festival.setVisibility(View.GONE);
        //fab_ADs.setVisibility(View.GONE);

        tv_close = findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);
        tv_setting = findViewById(R.id.tv_setting);
        tv_setting.setOnClickListener(this);

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        fragment = new WineryFragment();
        loadFragment(fragment);


        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Log.e("location", String.valueOf(location));
                if (location == null){
                    Toast.makeText(activity_Main.this, "Pleas check your Location Tract Permission", Toast.LENGTH_LONG);
                }else {
                    current_lat = location.getLatitude();
                    currend_long = location.getLongitude();
                    insertString("current_lat", String.valueOf(current_lat));
                    insertString("current_lon", String.valueOf(currend_long));

                    update_currentLocation(current_lat, currend_long);

                }
            }
        });

        //CheckPermission();
    }

    private void update_currentLocation(final double current_lat, final double currend_long) {

        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.USER_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        ResponseModel objRes = gson.fromJson(response, ResponseModel.class);
//                        if (objRes.getStatus().equals("200")) {
//                            Log.e("location_update", objRes.getMsg());
//                        }
                        Log.e("location_update", objRes.getMsg());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                        Methods.closeProgress();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                params.put(constant.LOCATION_LAT, String.valueOf(current_lat));
                params.put(constant.LOCATION_LON, String.valueOf(currend_long));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(activity_Main.this);
        requestQueue.add(res_companyInfo);

    }

    private void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }
    public void onResume(){
        super.onResume();
        //getLocation();
    }

    private void getLocation() {
        try {
            //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    protected void onPause(){
        super.onPause();
        //locationManager.removeUpdates(this);
    }

    public void onBackPressed() {

    }
    private boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        fragment = null;
        switch (item.getItemId()){
            case R.id.navigation_winery:
                fragment = new WineryFragment();
                //fab_Winery.setVisibility(View.VISIBLE);
                //fab_Event.setVisibility(View.GONE);
                //fab_Tour.setVisibility(View.GONE);
                //fab_Festival.setVisibility(View.GONE);
                //fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_event:
                fragment = new EventFragment();
                //fab_Winery.setVisibility(View.GONE);
                //fab_Event.setVisibility(View.VISIBLE);
                //fab_Tour.setVisibility(View.GONE);
                //.setVisibility(View.GONE);
                //fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_tour:
                fragment = new TourFragment();
                //fab_Winery.setVisibility(View.GONE);
                //fab_Event.setVisibility(View.GONE);
                //fab_Tour.setVisibility(View.VISIBLE);
                //fab_Festival.setVisibility(View.GONE);
                //fab_ADs.setVisibility(View.GONE);
                break;
            case R.id.navigation_festival:
                fragment = new FestivalFragment();
                //fab_Winery.setVisibility(View.GONE);
                //fab_Event.setVisibility(View.GONE);
                //fab_Tour.setVisibility(View.GONE);
                //fab_ADs.setVisibility(View.GONE);
                //fab_Festival.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_ads:
                fragment = new AdsFragment();
                //fab_Winery.setVisibility(View.GONE);
                //fab_Event.setVisibility(View.GONE);
                ///fab_Tour.setVisibility(View.GONE);
                //fab_Festival.setVisibility(View.GONE);
                //fab_ADs.setVisibility(View.VISIBLE);
                break;
        }
        return loadFragment(fragment);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab_My:
                Intent my_intent = new Intent(activity_Main.this, Activity_Before_My.class);
                startActivity(my_intent);
                break;
//            case R.id.fab_Winery:
//                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
//                Askpay_intent.putExtra(constant.PAYMONEY, "winery");
//                startActivity(Askpay_intent);
//                break;
            //case R.id.fab_Event:
            //    Enable_add_event();
            //    break;
//            case R.id.fab_Tour:
//                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
//                Askpay_intent.putExtra(constant.PAYMONEY, "tour");
//                startActivity(Askpay_intent);
//                break;
//            case R.id.fab_Festival:
//                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
//                Askpay_intent.putExtra(constant.PAYMONEY, "festival");
//                startActivity(Askpay_intent);
//                break;
//            case R.id.fab_ADs:
//                Askpay_intent = new Intent(activity_Main.this, activity_AskPay.class);
//                Askpay_intent.putExtra(constant.PAYMONEY, "ads");
//                startActivity(Askpay_intent);
//                break;
            case R.id.tv_setting:
                Intent setting_intent = new Intent(activity_Main.this, activity_Setting.class);
                startActivity(setting_intent);
                break;
            case R.id.tv_close:
                Intent signin_intent = new Intent(activity_Main.this, activity_SignIn.class);
                startActivity(signin_intent);
                finish();
                break;
        }
    }
    private void Enable_add_event() {
        final StringRequest srStatus = new StringRequest(Request.Method.POST, constant.ENABLE_ADD_NEW,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Methods.closeProgress();
                        Gson gson = new Gson();
                        responseModel = gson.fromJson(response, ResponseModel.class);
                        if(responseModel.getStatus().equals("200")){
                            Toast.makeText(activity_Main.this, responseModel.getMsg(), Toast.LENGTH_SHORT).show();
                            Intent event_intent = new Intent(activity_Main.this, Claim_event.class);
                            startActivity(event_intent);
                        }else {
                            Methods.showAlertDialog(responseModel.getMsg(), activity_Main.this);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_Main.this);
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(constant.EMAIL, getString(constant.EMAIL));
                return params;
            }
            @Override
            public Map<String, String> getHeaders() {
                return addHeader();
            }
        };
        MySingleton.getInstance(this).addToRequestQueue(srStatus);
    }
    private Map<String, String> addHeader() {
        HashMap<String, String> params = new HashMap<String, String>();
        String creds = String.format("%s:%s",constant.Auth_UserName, constant.Auth_Password);
        String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
        params.put("Authorization", auth);
        return params;
    }
    public synchronized String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    /**
     * Called when the location has changed.
     *
     * <p> There are no restrictions on the use of the supplied Location object.
     *
     * @param location The new location, as a Location object.
     */
    @Override
    public void onLocationChanged(Location location) {

    }

    /**
     * This callback will never be invoked and providers can be considers as always in the
     * {@link LocationProvider#AVAILABLE} state.
     *
     * @param provider
     * @param status
     * @param extras
     * @deprecated This callback will never be invoked.
     */
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    /**
     * Called when the provider is enabled by the user.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderEnabled(String provider) {

    }

    /**
     * Called when the provider is disabled by the user. If requestLocationUpdates
     * is called on an already disabled provider, this method is called
     * immediately.
     *
     * @param provider the name of the location provider associated with this
     *                 update.
     */
    @Override
    public void onProviderDisabled(String provider) {

    }
    public synchronized void insertString(String key, String value) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }
}