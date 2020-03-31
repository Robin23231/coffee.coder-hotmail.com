package com.demo.winery.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.demo.winery.R;
import com.demo.winery.utils.Methods;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GoogleMapFragment extends Fragment implements OnMapReadyCallback, LocationListener {
    private static final int FINE_LOCATION_PERMISSION_REQUEST = 1;
    private static final int CONNECTION_RESOLUTION_REQUEST = 2;

    private int PROXIMITY_RADIUS = 5000;

    GoogleMap mGoogleMap;
    MapView mMapView;
    LocationManager locationManager;
    double latitude;
    double longitude;
    LatLng current;
    String provider;
    Criteria criteria;

    public GoogleMapFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.layout_googlemap, container, false);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.googlemap);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;

        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        criteria = new Criteria();
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        provider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString() ;
        if(provider == null){
            Methods.showAlertDialog("Check your Google map access.", getContext());
        }else{
            if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                getActivity().requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else {
                mGoogleMap.setMyLocationEnabled(true);
            }
            Location location = locationManager.getLastKnownLocation(provider);
            if (location != null){
                Log.e("TAG", "GPS is on");
            }
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            current = new LatLng(latitude, longitude);

            StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&types=" + "vinery");
            googlePlacesUrl.append("&sensor=true");
            googlePlacesUrl.append("&key=" + R.string.google_maps_key);

            GooglePlacesReadTask googlePlacesReadTask = new GooglePlacesReadTask();
            Object[] toPass = new Object[2];
            toPass[0] = googleMap;
            toPass[1] = googlePlacesUrl.toString();
            googlePlacesReadTask.execute(toPass);
            mGoogleMap.addMarker(new MarkerOptions().position(current).title("Marker"));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLng(current));
            mGoogleMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQUEST: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mGoogleMap.setMyLocationEnabled(true);
                }
            }
        }
    }

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


}
