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

public class FavoriteActivity extends AppCompatActivity {


    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    ArrayList<String>  allTitels;
    FirebaseDatabase mDatabase;
    DatabaseReference myRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        // Necessary stuff for functionality
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        allTitels = new ArrayList<>();

        // Makes variable for the username
        final String currentUserDisplay = currentUser.getDisplayName();

        mDatabase = FirebaseDatabase.getInstance();

        // Makes the database start at the right place in branch
        myRef = mDatabase.getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/"+currentUserDisplay);


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Listens if there is made a change to the 'favorites' of the current user
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Creates a snapshot from the database to iterate through
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Log.v("Eerste key", "   " + dataSnapshot.toString()); // Log to check spot in branch

                    // Gets the right value and makes it able to split through because of the String
                    String values = null;
                    values = children.getValue().toString();



                    // Split's the list by ' , ' to make all movie titles with unique stand alone
                    String[] lijst = values.split(",");
                    Log.v("idk key", "   " + Arrays.toString(lijst)); // Log to check spot in branch

                    // Split's the remaining list for every unique one on ' = ' and adds all values from them to the new arraylist
                    for (int i = 0 ; i < lijst.length; i++ ){
                        String[] lijst2 = values.split("=");
                        allTitels.add(lijst2[i]);
                    }
//                    allTitels.get(allTitels.size() -1);


                    Log.v("keyResult","   " + allTitels); // Log to check result

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

                    // Makes the listview item's clickable
                    try {
                        mListView.setOnItemClickListener(
                                new AdapterView.OnItemClickListener() {
                                    @Override
                                    // Shows how to add the movie to favorites
                                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                        String itemPicked = "You have selected " + String.valueOf(adapterView.getItemAtPosition(position));
                                        Toast.makeText(FavoriteActivity.this, itemPicked, Toast.LENGTH_LONG).show();


                                        SharedPreferences settings = FavoriteActivity.this.getSharedPreferences("iditem", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = settings.edit();
                                        editor.putString("iditem", String.valueOf(adapterView.getItemAtPosition(position)));
                                        editor.commit();
                                        editor.apply();
                                        Log.v("yolo key", String.valueOf(adapterView.getItemAtPosition(position)));


                                        Intent detailIntent = new Intent(FavoriteActivity.this, RecipeDetailActivity.class);
                                        detailIntent.putExtra("id", String.valueOf(adapterView.getItemAtPosition(position)));

                                        startActivity(detailIntent);
                                    }

                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, ProfileActivity.class);
        intent.putExtra("InfoActivity", ProfileActivity.class);
        startActivity(new Intent(FavoriteActivity.this, ProfileActivity.class));
    }
}



