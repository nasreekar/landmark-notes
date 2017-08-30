package com.abhijith.home.landmark_notes;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Models.Notes;
import Models.NotesList;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    Button addNote;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseNotes;

    //Form Details
    EditText title;
    EditText description;
    TextView date;
    TextView locationTv;
    String currentDate;

    //For Location
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude,longitude;
    String finalLocation;

    //Notes List View
    ListView listViewNotes;
    List<Notes> notesList;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        //attach value event listener to database object
        databaseNotes.addValueEventListener(new ValueEventListener() {
            //executed every-time we change anything in database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                notesList.clear();

                for(DataSnapshot notesSnapshot: dataSnapshot.getChildren()){
                    Notes note = notesSnapshot.getValue(Notes.class);
                    Log.i("THE_CURRENT_NOTE:::", note.toString());
                    notesList.add(note);
                }

                NotesList adap = new NotesList(HomeActivity.this,notesList);
                listViewNotes.setAdapter(adap); //attaching it to notes list view

            }
            //will be executed if there is any error
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        databaseNotes = FirebaseDatabase.getInstance().getReference("notes"); //parent node

        logout = (Button)findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            }
        };

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });


        addNote = (Button)findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNotes();
            }
        });

        //Form Fields
        title = (EditText)findViewById(R.id.etTitle);
        description = (EditText)findViewById(R.id.etDescription);
        date = (TextView)findViewById(R.id.tvDate);
        locationTv = (TextView)findViewById(R.id.tvLocation);

        //getting the current date and time and displaying it on date text view
        currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        date.setText(currentDate);

        //getting the current location and displaying it on location text view
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        // list view of notes saved in database
        listViewNotes = (ListView)findViewById(R.id.lvNotes);
        notesList = new ArrayList<>();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocationDetails();
    }

    private void getLocationDetails() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

    //to get latitude and longitude
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                double lati = location.getLatitude();
                double longi = location.getLongitude();
                latitude = String.valueOf(lati);
                longitude = String.valueOf(longi);

                //To get the location name based on latitude and longitude
                String loc = getCityName(lati,longi);
                if(TextUtils.isEmpty(loc)){
                    locationTv.setText("Singapore"); // if the user clicks "No" in alert dialog,we use default text as Singapore

                }
                else{
                    locationTv.setText(loc);
                }

            }else{
                Toast.makeText(this,"Unable to Trace your location",Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.alertTitle)
                .setMessage(R.string.alertDescription)
                .setCancelable(false)
                .setPositiveButton(R.string.alertPositive, new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
//                .setNegativeButton(R.string.alertNegative, new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, final int id) {
//                        dialog.cancel();
//                    }
//                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void addNotes() {
        String heading = title.getText().toString().trim(); //title
        String desc = description.getText().toString().trim(); //description
        String userEmail = mAuth.getCurrentUser().getEmail();

        if(TextUtils.isEmpty(heading)){
            heading = "New Document";
        }else {
            String id = databaseNotes.push().getKey(); //auto generated id by firebase
            Notes note = new Notes(heading, desc,userEmail, currentDate, finalLocation, latitude, longitude);
            databaseNotes.child(id).setValue(note);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();
            refreshEditTexts();
        }

    }

    private String getCityName(double lati, double longi){
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocation(lati,longi,1);
            if (geocoder.isPresent()) {
                StringBuilder stringBuilder = new StringBuilder();
                if (addresses.size()>0) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
//                    String name = returnAddress.getFeatureName();
//                    String subLocality = returnAddress.getSubLocality();
                    String country = returnAddress.getCountryName();
//                    String region_code = returnAddress.getCountryCode();
//                    String zipcode = returnAddress.getPostalCode();
//                    String state = returnAddress.getAdminArea();

                    finalLocation = localityString + ", " + country;
                }
            } else {

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return finalLocation;
    }

    private void refreshEditTexts(){
        title.setText("");
        description.setText("");
    }
}
