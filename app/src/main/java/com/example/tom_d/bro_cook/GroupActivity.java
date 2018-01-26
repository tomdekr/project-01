package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tom_d.bro_cook.ProfileActivity;
import com.example.tom_d.bro_cook.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

public class GroupActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<String>  allTitels;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;
    DatabaseReference myRefGroup;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        // Necessary stuff for functionality
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        allTitels = new ArrayList<>();

        // Makes variable for the username
        final String currentUserDisplay = currentUser.getDisplayName();

        mDatabase = FirebaseDatabase.getInstance();

        // Makes the database start at the right place in branch
        myRef = mDatabase.getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/" + currentUserDisplay + ("/groupName/"));

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             // Listens if there is made a change to the 'favorites' of the current user
             public void onDataChange(DataSnapshot dataSnapshot) {
                 String data = null;
                 data = dataSnapshot.getValue().toString();
                 Log.v("Eerste key", "   " + data.toString()); // Log to check spot in branch


                 // Split's the list by ' , ' to make all movie titles with unique stand alone
                 String[] lijst = data.split(",");
                 Log.v("idk key", "   " + Arrays.toString(lijst)); // Log to check spot in branch

                 // Split's the remaining list for every unique one on ' = ' and adds all values from them to the new arraylist
                 for (int i = 0; i < lijst.length; i++) {
                     allTitels.add(lijst[i]);
                 }


                 String infogroup = allTitels.toString().replace("[", "").replace("]", "");
                 Log.v("infoResult", "   " + infogroup); // Log to check result

                 myRefGroup = mDatabase.getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/groupNames/" + infogroup + ("/"));


                 myRefGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     // Listens if there is made a change to the 'favorites' of the current user
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                             String data = null;
                             data = dataSnapshot2.getValue().toString();
                             Log.v("Eerste key", "   " + data.toString()); // Log to check spot in branch

                             String[] lijst = data.split("=");
                             String lijst2 = Arrays.toString(lijst);
                             String[] lijst3 = lijst2.split(",");


//                String[] lijst3 = test.split("=");

                         // Split's the remaining list for every unique one on ' = ' and adds all values from them to the new arraylist
                         for (int i = 0; i < lijst3.length; i++) {

                             allTitels.add(lijst3[i]);

                         }



                         Log.v("keyResult", "   " + allTitels); // Log to check result


                         // Makes the arraylist from api visible in a row_layout
                         ArrayAdapter<String> adapter =
                                 new ArrayAdapter<String>(
                                         getApplicationContext(),
                                         R.layout.row_layout,
                                         allTitels
                                 );
                         final ListView mListView = findViewById(R.id.listViewMovies2);

                         //Sets the adapter to make the final visualisation for the listview
                         mListView.setAdapter(adapter);
                     }}

                     @Override
                     public void onCancelled(DatabaseError databaseError) {

                     }
                 });
             }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });}


            // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("InfoActivity", ProfileActivity.class);
        startActivity(new Intent(GroupActivity.this, MainActivity.class));
    }
}



