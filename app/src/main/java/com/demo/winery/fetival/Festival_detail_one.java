package com.demo.winery.fetival;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.activities.activity_Link;
import com.demo.winery.model.Company_content;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.winery.utils.constant.MAPVIEW_BUNDLE_KEY;

public class Festival_detail_one  extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback {

    //MapView mapView;
    String id, zip;
    TextView tv_festivalName, festival_cost, tv_des_con,tv_address_c, tv_city_c, tv_state_c, tv_zip_c, tv_ads_c, tv_phone_c, tv_email_c, period;


    Company_content objRes;
    ImageView img_logo;
    Button btn_festival_claim;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Methods.closeProgress();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_festival_detail);

        Intent data = getIntent();
        id = data.getStringExtra(constant.ID);
        zip = data.getStringExtra(constant.ZIP);
        tour_info_progress(id);

        tv_festivalName = findViewById(R.id.tv_festivalName);
        festival_cost = findViewById(R.id.festival_cost);
        tv_des_con = (TextView) findViewById(R.id.tv_des_con);
        tv_address_c = (TextView) findViewById(R.id.tv_address_c);
        tv_city_c = (TextView) findViewById(R.id.tv_city_c);
        tv_state_c = (TextView) findViewById(R.id.tv_state_c);
        tv_zip_c = (TextView) findViewById(R.id.tv_zip_c);
        tv_ads_c = (TextView) findViewById(R.id.tv_ads_c);
        //tv_phone_t = (TextView) findViewById(R.id.tv_phone_t);
        //tv_email_t = (TextView) findViewById(R.id.tv_email_t);
        tv_phone_c = (TextView) findViewById(R.id.tv_phone_c);
        //view_rate = (RatingBar) findViewById(R.id.view_rate);
        //no_rating = (TextView) findViewById(R.id.no_rating);
        period = (TextView) findViewById(R.id.period);
        img_logo = findViewById(R.id.img_logo);

        tv_phone_c.setOnClickListener(this);

        btn_festival_claim = (Button) findViewById(R.id.btn_festival_claim);
        btn_festival_claim.setOnClickListener(this);

        //mapView = (MapView) findViewById(R.id.mapView);
        //initGoogleMap(savedInstanceState);
    }

    private void tour_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.FESTIVAL_GETINFO,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            tv_festivalName.setText(objRes.getfestival_name());
                            period.setText(objRes.getfestival_date()+" ~ "+objRes.getfestival_time());
                            if (objRes.getfestival_description().equals("")){
                                tv_des_con.setText("<No Description>");
                            }else {
                                tv_des_con.setText(objRes.getfestival_description());
                            }
                            festival_cost.setText(objRes.getfestival_cost() + " $ ");
                            tv_address_c.setText(objRes.getfestival_address());
                            tv_city_c.setText(objRes.getfestival_city());
                            tv_state_c.setText(objRes.getfestival_state());
                            tv_zip_c.setText(objRes.getfestival_zip_code());
                            tv_ads_c.setText(objRes.getfestival_website_url());
                            //tv_phone_t.setText(objRes.getfestival_phone());
                            byte[] imageBytes = Base64.decode(objRes.getfestival_logo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_logo.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(Festival_detail_one.this, "No Review", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error_signup", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Festival_detail_one.this);
        requestQueue.add(res_companyInfo);
    }

    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        //mapView.onCreate(mapViewBundle);
        //mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {

        int height = 150;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.festival_logo);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng address_location = getLocationFromAddress(Festival_detail_one.this, zip);
        map.addMarker(new MarkerOptions().position(address_location).title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 13));
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
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        //mapView.onSaveInstanceState(mapViewBundle);
    }
    @Override
    public void onResume() {
        super.onResume();
       // mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        //mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        //mapView.onStop();
    }
    @Override
    public void onPause() {
        //mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
       // mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
    }

    public String getString(String key) {
        SharedPreferences mSharedPreferences = getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_festival_claim:
                Intent progressIntent = new Intent(Festival_detail_one.this, activity_Link.class);
                progressIntent.putExtra("LINK", "https://wineryfinder.org/signups/festivals");
                startActivity(progressIntent);
                break;
        }
    }

}
