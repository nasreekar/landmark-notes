package com.abhijith.home.landmark_notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import Models.Notes;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Notes notz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        notz = (Notes) getIntent().getSerializableExtra("serialize_object_data");
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

        Double lat;
        Double longi;
        final String mTitle;
        final String desc;
        final String place;

        //if notz is empty, then set the maps with default values
        if(notz==null){
            lat = -33.8718356;
            longi = 151.1521636;
            mTitle = "Sydney";
            desc = "Default Description";
            place = "Sydney";

        }else{
            lat = Double.valueOf(notz.getLatitude());
            longi = Double.valueOf(notz.getLongitute());
            mTitle = notz.getTitle();
            desc = notz.getDescription();
            place = notz.getLocation();
        }

        // Add a marker in correct location and move the camera
        LatLng loc = new LatLng( lat,longi);
        mMap.addMarker(new MarkerOptions().position(loc).title(mTitle));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(loc)
                .zoom(12).build();
        //Zoom in and animate the camera.
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                AlertDialog alertDialog = new AlertDialog.Builder(MapsActivity.this).create();
                alertDialog.setTitle("More Details of " + "\""+ mTitle+"\"");
                alertDialog.setMessage(desc + "\n \n" + place);
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Got It!",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            }
        });
    }
}
