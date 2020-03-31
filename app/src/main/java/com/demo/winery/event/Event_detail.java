package com.demo.winery.event;

import android.content.Context;
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
import com.demo.winery.model.Company_content;
import com.demo.winery.utils.Methods;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.demo.winery.utils.constant.MAPVIEW_BUNDLE_KEY;

public class Event_detail extends AppCompatActivity implements View.OnClickListener , OnMapReadyCallback {
    String id, zip;
    MapView event_mapView;
    Company_content objRes;
    TextView tv_eventName, tv_period, tv_description, tv_location;
    ImageView logo;
    TextView winery_name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Methods.closeProgress();
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.layout_event_detail);

        tv_eventName = findViewById(R.id.tv_eventName);
        tv_period = findViewById(R.id.tv_period);
        tv_description = findViewById(R.id.tv_description);
        tv_location = findViewById(R.id.tv_location);
        logo = findViewById(R.id.logo);
        winery_name = findViewById(R.id.winery_name);

        Bundle extras = getIntent().getExtras();
        id = extras.getString(constant.ID);
        zip = extras.getString(constant.ZIP);

        Log.e("event_id", id);
        Log.e("event_zip", zip);

        event_mapView = (MapView) findViewById(R.id.event_mapView);
        initGoogleMap(savedInstanceState);

        event_info_progress(id);
    }

    private void event_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.EVENT_DETAIL_ONE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            if (objRes.getevent_name().equals("")){
                                tv_eventName.setText("<No Event Name>");
                            }else {
                                tv_eventName.setText(objRes.getevent_name());
                            }
                            tv_period.setText(objRes.getdate()+"-"+objRes.getstart_time()+" ~ "+objRes.getdate()+"-"+objRes.getend_time());
                            tv_description.setText(objRes.getdescription_event());
                            tv_location.setText(objRes.getlocation_event());
                            winery_name.setText(objRes.getevent_winery_name());

                            byte[] imageBytes = Base64.decode(objRes.getevent_logo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            logo.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(Event_detail.this, "No Details", Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Methods.showAlertDialog(getString(R.string.error_network_check), activity_SignUp.this);
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(res_companyInfo);
    }


    @Override
    public void onClick(View v) {}
    private void initGoogleMap(Bundle savedInstanceState){
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        event_mapView.onCreate(mapViewBundle);
        event_mapView.getMapAsync(this);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        event_mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        event_mapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        event_mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        event_mapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {

        int height = 150;
        int width = 100;
        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng address_location = getLocationFromAddress(Event_detail.this, zip);
        map.addMarker(new MarkerOptions().position(address_location).title("Marker").icon(BitmapDescriptorFactory.fromBitmap(smallMarker)));
        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
    }

    @Override
    public void onPause() {
        event_mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        event_mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        event_mapView.onLowMemory();
    }

    //how to get Latitude and Longtitude from address
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
}
