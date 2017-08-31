package com.abhijith.home.landmark_notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Models.Notes;
import Models.NotesList;

public class HomeActivity extends AppCompatActivity {

//    Button logout;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference databaseNotes;

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

//        logout = (Button)findViewById(R.id.logoutBtn);
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(HomeActivity.this, MainActivity.class));
                }
            }
        };

//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mAuth.signOut();
//            }
//        });

        // list view of notes saved in database
        listViewNotes = (ListView)findViewById(R.id.lvNotes);
        notesList = new ArrayList<>();
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
                startActivity(new Intent(HomeActivity.this, AddNoteActivity.class));
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
            case R.id.recycle:
                startActivity(new Intent(this, RecyclerNoteListActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
