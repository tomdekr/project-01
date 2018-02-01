package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_CREATOR;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_INT;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_URL;

/**
 * Displays the Favorite recipes from a user
 */

public class FavoriteList extends AppCompatActivity implements Adapter.OnItemClickListener {
    public static final String EXTRA_ID = "id";
    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    DatabaseReference mDatabaseFavorites;
    DatabaseReference mDatabaseGroup;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        // Necessary code for functionality
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String currentUserDisplay = user.getDisplayName();
        mDatabaseFavorites = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/"+currentUserDisplay+"/");
        mDatabaseGroup = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/groupNames/");
        mRecipeList = new ArrayList<>();

        // Code for making the RecyclerView with certain layout
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FavoriteList.this));


    }


    @Override
    protected void onStart() {
        super.onStart();

        // Code for a listener that gets the value's from Firebase Database Reference
        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecipeList.clear();
                // Creates a snapshot from the database to iterate through
                for (DataSnapshot ds : dataSnapshot.getChildren())
                {
                    // Gets the right value and adds it to the right variable
                    String id = (String) ds.child("id").getValue();
                    String imageUrl = (String) ds.child("imageUrl").getValue();
                    String recipe = (String) ds.child("recipe").getValue();
                    String rating = (String) ds.child("rating").getValue();

                    // Adds the values for mRecipeList as class type Recipe
                    mRecipeList.add(new Recipe(imageUrl, recipe, id, rating));


                }
                // Makes the mRecipeList from api values visible with the adapter
                mExampleAdapter = new Adapter(FavoriteList.this, mRecipeList);

                //Sets the adapter to make the final visualisation for the RecyclerView
                mRecyclerView.setAdapter(mExampleAdapter);

                // Sets a on Item Listener for the method OnItemClick
                mExampleAdapter.setOnItemClickListener(FavoriteList.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    // Method that produces the function when an Item in the RecyclerView is clicked
    @Override
    public void onItemClick(int position) {
        // Creates a new Intent
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);

        // Gets the position of the item clicked, to know which values to store in the intent from the right item
        Recipe clickedItem = mRecipeList.get(position);
        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getRecipe());
        detailIntent.putExtra(EXTRA_INT, clickedItem.getRating());

        // Intent for backpress
        detailIntent.putExtra("FavoriteActivity", "FavoriteList");

        // Starts the new Activity
        startActivity(detailIntent);
    }


    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(FavoriteList.this, MainActivity.class));
    }
}
