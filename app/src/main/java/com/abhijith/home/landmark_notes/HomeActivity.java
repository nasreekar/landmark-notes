package com.abhijith.home.landmark_notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

import Models.Notes;

public class HomeActivity extends AppCompatActivity {

    Button logout;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    Button addNote;
    DatabaseReference databaseNotes;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
    }

    private void addNotes() {
        String title = "Sample1";
        String description = "loremipsum";

        if (!TextUtils.isEmpty(title)) {

            String id = databaseNotes.push().getKey();

            Notes note = new Notes(title, description,mAuth.getCurrentUser().getEmail(), new Date(), "Singapore", "1.3484313", "103.8710033");
            databaseNotes.child(id).setValue(note);
            Toast.makeText(this, "Note added", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Please enter a title", Toast.LENGTH_LONG).show();
        }
    }
}
