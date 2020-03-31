package com.demo.winery.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.demo.winery.R;
import com.demo.winery.activities.activity_Details;
import com.demo.winery.model.Company_content;
import com.demo.winery.utils.constant;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class Tab1Fragment extends Fragment implements View.OnClickListener {

    TextView tv_des_con,tv_address_c, tv_city_c, tv_state_c, tv_zip_c,  tv_phone_c, tv_email_c;

    ScrollView scroll_content;
    RatingBar view_rate;
    private String id;
    String strtext;
    TextView no_rating, winery_name;
    Company_content objRes;
    ImageView img_logo;
    private TextView distance_view;
    //MapView mapView;
    GoogleMap mMap;
    double current_latitude, current_longtitude, distance_value;
    LatLng address_location;
    String distance, zip;
    private ImageView mapPreview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tv_des_con = (TextView) view.findViewById(R.id.tv_des_con);
        tv_address_c = (TextView) view.findViewById(R.id.tv_address_c);
        tv_city_c = (TextView) view.findViewById(R.id.tv_city_c);
        tv_state_c = (TextView) view.findViewById(R.id.tv_state_c);
        tv_zip_c = (TextView) view.findViewById(R.id.tv_zip_c);

        tv_phone_c = (TextView) view.findViewById(R.id.tv_phone_c);
        tv_email_c = (TextView) view.findViewById(R.id.tv_email_c);
        view_rate = (RatingBar) view.findViewById(R.id.view_rate);
        no_rating = (TextView) view.findViewById(R.id.no_rating);
        winery_name = view.findViewById(R.id.winery_name);
        img_logo = view.findViewById(R.id.img_logo);

        mapPreview = (ImageView) view.findViewById(R.id.map_image);

        distance_view = view.findViewById(R.id.map_distance);
       // mapView = (MapView) view.findViewById(R.id.mapView);
        //initGoogleMap(savedInstanceState);







        activity_Details activity_details = (activity_Details) getActivity();
        id = activity_details.id_selector();
        zip = activity_details.get_zip();
        distance = activity_details.get_distance();
        winery_info_progress(id);

        distance_view.setText(distance+" Miles");

        tv_phone_c.setOnClickListener(this);
        tv_email_c.setOnClickListener(this);
    }


//    private void initGoogleMap(Bundle savedInstanceState){
//        Bundle mapViewBundle = null;
//        if (savedInstanceState != null) {
//            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
//        }
//        //mapView.onCreate(mapViewBundle);
//        mapView.getMapAsync(this);
//    }
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

//    @Override
//    public void onMapReady(GoogleMap map) {
//        mMap = map;
//        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        mMap.setMyLocationEnabled(true);
//        int height = 150;
//        int width = 100;
//        BitmapDrawable bitmapdraw = (BitmapDrawable)getResources().getDrawable(R.drawable.marker);
//        Bitmap b = bitmapdraw.getBitmap();
//        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
//
//
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener(){
//            @Override
//            public void onMyLocationChange(Location location) {
//
//                current_latitude = location.getLatitude();
//                current_longtitude = location.getLongitude();
//
//            }
//        });
//
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//        address_location = getLocationFromAddress(getContext(), zip);
//        mMap.addMarker(new MarkerOptions().position(address_location).title("Marker"));
//        //map.moveCamera(CameraUpdateFactory.newLatLng(address_location));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address_location, 15));
//        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
//            @Override
//            public void onMapLoaded() {
//                // Make a snapshot when map's done loading
//                mMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
//                    @Override
//                    public void onSnapshotReady(Bitmap bitmap) {
//                        Bitmap bmpGrayscale = Bitmap.createBitmap(bitmap.getWidth(),
//                                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//                        Canvas canvas = new Canvas(bmpGrayscale);
//                        Paint paint = new Paint();
//
//                        ColorMatrix cm = new ColorMatrix();
//                        cm.setSaturation(2);
//                        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//                        paint.setColorFilter(f);
//                        canvas.drawBitmap(bitmap, 0, 0, paint);
//
//                        mapPreview.setLayoutParams(new RelativeLayout.LayoutParams(
//                                ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT
//                        ));
//                        mapPreview.setImageBitmap(bmpGrayscale);
//                        mapView.removeAllViews();
//                        // If map won't be used afterwards, remove it's views
//                        //     ((FrameLayout)findViewById(R.id.map)).removeAllViews();
//                    }
//                });
//            }
//        });
//        distance_value = distance(current_latitude, current_longtitude, address_location.latitude, address_location.longitude);
//        //distance_view.setText(String.valueOf(Math.round(distance_value)) + " Mile");
//    }

    @Override
    public void onPause() {
        //mapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        //mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        //mapView.onLowMemory();
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

//    private int distance(double lat1, double lon1, double lat2, double lon2) {
//        double theta = lon1 - lon2;
//        double dist = Math.sin(deg2rad(lat1))
//                * Math.sin(deg2rad(lat2))
//                + Math.cos(deg2rad(lat1))
//                * Math.cos(deg2rad(lat2))
//                * Math.cos(deg2rad(theta));
//        dist = Math.acos(dist);
//        dist = rad2deg(dist);
//        dist = dist * 60 * 1.1515;
//        int dist_int = (int) dist;
//        return (dist_int);
//    }

//    private double deg2rad(double deg) {
//        return (deg * Math.PI / 180.0);
//    }
//
//    private double rad2deg(double rad) {
//        return (rad * 180.0 / Math.PI);
//    }







    private void winery_info_progress(final String id) {
        StringRequest res_companyInfo = new StringRequest(Request.Method.POST, constant.WINERY_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();

                        objRes = gson.fromJson(response, Company_content.class);

                        if (objRes.getStatus().equals("200")) {
                            tv_des_con.setText(objRes.getDescription());
                            tv_address_c.setText(objRes.getAddress());
                            tv_city_c.setText(objRes.getCity());
                            tv_state_c.setText(objRes.getState());
                            tv_zip_c.setText(objRes.getZip());

                            if (objRes.getstars() == 0){
                                view_rate.setVisibility(View.GONE);
                                no_rating.setText("No reviews");
                            }else {
                                view_rate.setRating(objRes.getstars());
                                no_rating.setVisibility(View.GONE);
                            }
                            winery_name.setText(objRes.getCompany());
                            byte[] imageBytes = Base64.decode(objRes.getlogo(), Base64.DEFAULT);
                            Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            img_logo.setImageBitmap(decodedImage);

                        }else {
                            Toast.makeText(getActivity(), "No Review", Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(res_companyInfo);
    }


    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_phone_c:
//                Snackbar.make(v, "Here's a tv_phone_c", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                callIntent.setData(Uri.parse("tel:"+objRes.getPhone()));
//
//                if (ActivityCompat.checkSelfPermission(getContext(),
//                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
//                    return;
//                }
//                startActivity(callIntent);


                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:"+objRes.getPhone()));

                if (ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(callIntent);


                break;
            case R.id.tv_email_c:
//                Snackbar.make(v, "Here's a tv_email_c", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                String[] TO = {objRes.getEmail()};
                String[] CC = {getString(constant.EMAIL)};

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");


                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    getActivity().getFragmentManager().popBackStack();
                    Log.i("Finished sending email...", "");
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getContext(),
                            "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
    public String getString(String key) {
        SharedPreferences mSharedPreferences = getActivity().getSharedPreferences(constant.LOGIN_PREF, Context.MODE_PRIVATE);
        String  selected =  mSharedPreferences.getString(key, "");
        return selected;
    }

    public static boolean isNumeric(final String str) {

        // null or empty
        if (str == null || str.length() == 0) {
            return false;
        }

        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }

        return true;

    }

}