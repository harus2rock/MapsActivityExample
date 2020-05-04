package com.example.mapsactivityexample;

import androidx.fragment.app.FragmentActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  LatLng latlng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 皇居
        double latitude = 35.68;
        double longitude = 139.76;

        latlng = new LatLng(latitude, longitude);

        // a standard marker
        setMarker(latitude, longitude);

        // Icon
//        setIcon(latitude, longitude);

        // set tap listener
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng tapLocation) {
                // tap point's location
                latlng = new LatLng(tapLocation.latitude, tapLocation.longitude);
                String str = String.format(Locale.US, "%f, %f", tapLocation.latitude, tapLocation.longitude);
                mMap.addMarker(new MarkerOptions().position(latlng).title(str));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 14));
            }
        });

        // set long tap listener
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng longpushLocation) {
                LatLng newlocation = new LatLng(longpushLocation.latitude, longpushLocation.longitude);
                mMap.addMarker(new MarkerOptions().position(newlocation).title(""+longpushLocation.latitude+""+longpushLocation.longitude));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 14));

            }
        });
    }

    private void setMarker(double latitude, double longitude){
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latlng);
        markerOptions.title("Marker");
        mMap.addMarker(markerOptions);

        // zoom
        zoomMap(latitude, longitude);

    }

    private void setIcon(double latitude, double longitude){
        // Create BitmapDescriptor
        // you have to get an image yourself
        BitmapDescriptor descriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW);

        // Paste
        GroundOverlayOptions overlayOptions = new GroundOverlayOptions();
        overlayOptions.image(descriptor);

        //　public GroundOverlayOptions anchor (float u, float v)
        // (0,0):top-left, (0,1):bottom-left, (1,0):top-right, (1,1):bottom-right
        overlayOptions.anchor(0.5f, 0.5f);

        // Image size (meter)
        // public GroundOverlayOptions	position(LatLng location, float width, float height)
        overlayOptions.position(latlng, 300f, 300f);

        // paste
        GroundOverlay overlay = mMap.addGroundOverlay(overlayOptions);
        // zoom
        zoomMap(latitude, longitude);

        // transparency
        overlay.setTransparency(0.0F);
    }

    private void zoomMap(double latitude, double longitude){
        // viewable latitude and longitude
        double south = latitude * (1-0.00005);
        double west = longitude * (1-0.00005);
        double north = latitude * (1+0.00005);
        double east = longitude * (1+0.00005);

        // LatLngBounds (LatLng southwest, LatLng northeast)
        LatLngBounds bounds = LatLngBounds.builder()
                .include(new LatLng(south, west))
                .include(new LatLng(north, east))
                .build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;

        // static CameraUpdate.newLatLngBounds(LatLngBounds bounds, int width, int height, int padding)
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, width, height, 0));
    }
}
