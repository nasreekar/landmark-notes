package com.abhijith.home.landmark_notes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;

import java.util.List;

import Models.Notes;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Gson gson = new Gson();

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

        String tag = getIntent().getStringExtra("tag");

        // all notes on map
        if(tag.contentEquals("all_notes")){

            List<Notes> noteList = getNoteListFromIntent();
            if(noteList != null){
                if(noteList.size() > 0){
                    showClusterMapView(noteList, mMap);
                }
            }
        // my notes on map
        }else if(tag.contentEquals("my_notes")){
            List<Notes> myNoteList = getNoteListFromIntent();
            if(myNoteList != null){
                if(myNoteList.size() > 0){
                    showClusterMapView(myNoteList, mMap);
                }
            }
        }
        //single note on map
        else if(tag.contentEquals("single_note")){

            Notes note = (Notes) getIntent().getSerializableExtra("serialize_object_data");
            if(note != null){
                showSingleNoteOnMap(note, mMap);
            }
        }


    }

    private void showSingleNoteOnMap(Notes note, GoogleMap mMap) {
        Double lat;
        Double longi;
        final String mTitle;
        final String desc;
        final String place;

        //if notz is empty, then set the maps with default values
        if(note==null){
            lat = -33.8718356;
            longi = 151.1521636;
            mTitle = "Sydney";
            desc = "Default Description";
            place = "Sydney";

        }else{
            lat = Double.valueOf(note.getLatitude());
            longi = Double.valueOf(note.getLongitute());
            mTitle = note.getTitle();
            desc = note.getDescription();
            place = note.getLocation();
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

    private void showClusterMapView(List<Notes> noteList, GoogleMap mMap) {

        ClusterManager<Notes> mClusterManager = new ClusterManager<>(this, mMap);
        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
        mMap.setOnInfoWindowClickListener(mClusterManager);
        addNoteItemsWhichHasLatLng(mClusterManager, noteList);
        //mClusterManager.addItems(noteList);
        mClusterManager.cluster();
    }

    private void addNoteItemsWhichHasLatLng(ClusterManager<Notes> mClusterManager, List<Notes> noteList) {
        for(Notes note : noteList){
            if(note.getLocation() != null){
                mClusterManager.addItem(note);
            }
        }
    }

    private List<Notes> getNoteListFromIntent(){
        String intentString = getIntent().getStringExtra("note_list");

        if(!TextUtils.isEmpty(intentString)){
            try{
                TypeToken<List<Notes>> token = new TypeToken<List<Notes>>() {};
                return gson.fromJson(intentString, token.getType());
            }catch (Exception ex){
                Log.e("MapsActivity", "Failed to get noteList from intent");
            }
        }

        return null;
    }
}
