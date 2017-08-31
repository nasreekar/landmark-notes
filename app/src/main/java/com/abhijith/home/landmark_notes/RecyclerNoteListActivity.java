package com.abhijith.home.landmark_notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.MyRecyclerAdapter;
import Models.Notes;

public class RecyclerNoteListActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseNotes;


    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Notes> listitems;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        //attach value event listener to database object
        databaseNotes.addValueEventListener(new ValueEventListener() {
            //executed every-time we change anything in database
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listitems.clear();

                for(DataSnapshot notesSnapshot: dataSnapshot.getChildren()){
                    Notes note = notesSnapshot.getValue(Notes.class);
                    Log.i("THE_CURRENT_NOTE:::", note.toString());
                    listitems.add(note);
                }

                adapter = new MyRecyclerAdapter(RecyclerNoteListActivity.this,listitems);
                recyclerView.setAdapter(adapter);

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
        setContentView(R.layout.activity_recycler_note_list);

        recyclerView = (RecyclerView)findViewById(R.id.recyclerViewList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseNotes = FirebaseDatabase.getInstance().getReference("notes"); //parent node
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(RecyclerNoteListActivity.this, MainActivity.class));
                }
            }
        };


        listitems = new ArrayList<>();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return true;
    }

    public void logoutClicked(){
        mAuth.signOut();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_note:
                startActivity(new Intent(RecyclerNoteListActivity.this, AddNoteActivity.class));
                return true;
            case R.id.action_my_geo_notes:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.action_all_geo_notes:
                startActivity(new Intent(this, MapsActivity.class));
                return true;
            case R.id.logout:
                logoutClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
