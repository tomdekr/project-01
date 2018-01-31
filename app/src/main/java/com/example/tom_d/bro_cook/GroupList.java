package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_CREATOR;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_INT;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_URL;

public class GroupList extends AppCompatActivity implements Adapter.OnItemClickListener {
    public static final String EXTRA_ID = "id";
    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    DatabaseReference mDatabaseFavorites;
    DatabaseReference mDatabaseGroup;
    DatabaseReference databaseBrancheGroupCheck;
    FirebaseAuth mAuth;
    ArrayList<String>  allTitels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        // Necessary code for functionality
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String currentUserDisplay = user.getDisplayName();
        mDatabaseFavorites = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/" + currentUserDisplay + ("/groupName/"));
        mRecipeList = new ArrayList<>();
        allTitels = new ArrayList<>();

        // Code for making the RecyclerView with certain layout
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GroupList.this));
        mRecyclerView.setHasFixedSize(true);

    }

    @Override
    protected void onStart() {
        super.onStart();
        parseList();

    }

    public void parseList(){
        // Code for a listener that gets the value's from Firebase Database Reference
        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = null;
                data = dataSnapshot.getValue().toString();

                // Splits the list by ' , ' to make all values stand alone
                String[] lijst = data.split(",");

                // Adds all values from them to the new Arraylist
                for (int i = 0; i < lijst.length; i++) {
                    allTitels.add(lijst[i]);
                }

                // Cleans the obtained value and replaces targets to create the right groupName for the right User
                String infogroup = allTitels.toString().replace("[", "").replace("]", "");
                Log.v("infoResult", "   " + infogroup); // Log to check result

                // Creates the right Database Reference
                mDatabaseGroup = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/groupNames/" + infogroup + ("/"));

                // Code for a listener that gets the value's from Firebase Database Reference
                mDatabaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    // Code for a listener that gets the value's from Firebase Database Reference
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mRecipeList.clear();
                        // Creates a snapshot from the database to iterate through
                        for (DataSnapshot ds : dataSnapshot.getChildren()) //--> At this point, ds is an iterator of dataSnapshot; it will iterate the dataSnapshot's children. In this case, the first child's type is String, thus the first iteration of ds will have a type of String.
                        {
                            // Gets the right value and adds it to the right variable
                            String id = (String) ds.child("id").getValue();
                            String imageUrl = (String) ds.child("imageUrl").getValue();
                            String recipe = (String) ds.child("recipe").getValue();//--> at this point, you tried to assign a String to an Object with type "protest" by conversion. This is illegal, so it will throw an exception instead.
                            String rating = (String) ds.child("rating").getValue();

                            // Adds the values for mRecipeList as class type Recipe
                            mRecipeList.add(new Recipe(imageUrl, recipe, id,rating));

                        }
                        // Makes the mRecipeList from api values visible with the adapter
                        mExampleAdapter = new Adapter(GroupList.this, mRecipeList);

                        //Sets the adapter to make the final visualisation for the RecyclerView
                        mRecyclerView.setAdapter(mExampleAdapter);

                        // Sets a on Item Listener for the method OnItemClick
                        mExampleAdapter.setOnItemClickListener(GroupList.this);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });}



    // Method that produces the function when an Item in the RecyclerView is clicked
    @Override
    public void onItemClick(int position) {
        // Creates a new Intent
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);

        // Gets the position of the item clicked, to know which values to store in the intent from the right item
        Recipe clickedItem = mRecipeList.get(position);
        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra("id",clickedItem.getId());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getRecipe());
        detailIntent.putExtra(EXTRA_INT, clickedItem.getRating());

        // Intent for backpress
        detailIntent.putExtra("GroupAcivity", "GroupList");

        // Starts the new Activity
        startActivity(detailIntent);

    }

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(GroupList.this, MainActivity.class));
    }



}

