package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




public class RecipeDetailActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "id";
    private RequestQueue mRequestQueue;
    DatabaseReference mDatabaseBrancheUser;
    DatabaseReference mDatabaseBrancheGroup;
    DatabaseReference mDatabaseBrancheUserCheck;
    DatabaseReference mDatabaseBrancheGroupCheck;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Necessary code for functionality
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        mDatabaseBrancheUser = FirebaseDatabase.getInstance().getReference("userInfo");
        mDatabaseBrancheGroup = FirebaseDatabase.getInstance().getReference("groupNames");

        // Creates volley request with api
        mRequestQueue = Volley.newRequestQueue(this);

        if (findViewById(R.id.imageView6).getVisibility() == View.INVISIBLE){
            findViewById(R.id.imageView5).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Adds the recipe to favorites if image 'heart' is clicked
                    addToFavorite();
                    findViewById(R.id.imageView6).setVisibility(View.VISIBLE);



                }
            });}

        findViewById(R.id.imageView6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Removes the recipe from favorites if image 'heart' is clicked
                if (findViewById(R.id.imageView6).getVisibility() == View.VISIBLE) {
                    removeFromFavorites();
                    findViewById(R.id.imageView6).setVisibility(View.INVISIBLE);

                }
            }
        });

        findViewById(R.id.buttonGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adds the movie to favorites if image 'heart' is clicked
                addToGroupFavorites();
            }
        });


        findViewById(R.id.buttonGroup).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                removeFromGroup();
                return true;
            }
        });

        checkFavorites();
        parseJSON();

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFavorites();
    }

    // Method for making the visualisation with the info from the api about any clicked recipe
    private void parseJSON() {
        Intent intent = getIntent();
        String input = intent.getStringExtra(EXTRA_ID);
        String id = intent.getStringExtra("id");
        String broId = intent.getStringExtra("broId");
        String url = ("http://api.yummly.com/v1/api/recipe/recipe-id?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e");

        // Replaces any space in url (input) for underscore to prevent error
        url = url.replace("recipe-id", String.valueOf(input));

        if (input != null){
            findViewById(R.id.imageView6).setVisibility(View.INVISIBLE);
        }

        // Makes the url with the intent data from FavoriteList or GroupList
        if (id != null){
            url = url.replace("recipe-id", id);
        }
        if (broId != null){
            url = url.replace("recipe-id", broId);
        }

        // Request a JsonObject response from the provided URL.
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Gets the right Json Array to get data from
                            JSONArray jsonArray = response.getJSONArray("images");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject matches = jsonArray.getJSONObject(i);
                                String imageUrl = matches.getString("hostedLargeUrl");
                                Log.v("images", imageUrl);

                                // Sets the received image from recipe to imageView
                                ImageView imageView = findViewById(R.id.imageView);
                                Picasso.with(RecipeDetailActivity.this).load(imageUrl).fit().centerInside().into(imageView);
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        try{
                            // Gets the Json Object to get data from
                            JSONObject jsonObjectSource = response.getJSONObject("source");

                            // Gets the data from Json Object as strings
                            String recipe = response.getString("name");
                            final String prepTime = response.getString("totalTime");
                            String ingredients = response.getString("ingredientLines");
                            final String sourceUrl = jsonObjectSource.getString("sourceRecipeUrl");
                            int rating = response.getInt("rating");

                            // Replaces unwanted chars
                            ingredients = ingredients
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace("\"","")
                                    .replace(",","\n");

                            // Creates subtitles for the data
                            String prepTimeText = "Preparation time: "+ prepTime;
                            String ingredientsText = "Ingredients used: "+ "\n" + ingredients;
                            String sourceLinkText = "Recipe link: " + sourceUrl;
                            String ratingText = "Rating: " + rating;

                            Log.v("prep", prepTimeText);
                            Log.v("name", recipe);
                            Log.v("ingredients", ingredientsText);
                            Log.v("source", sourceLinkText);


                            TextView recipeName = findViewById(R.id.textView);
                            TextView prepTimeString = findViewById(R.id.textView2);
                            TextView ingredientsTextView = findViewById(R.id.textView3);
                            TextView ratingTextView = findViewById(R.id.textView5);

                            recipeName.setText(recipe);
                            prepTimeString.setText(prepTimeText);
                            ingredientsTextView.setMovementMethod(new ScrollingMovementMethod());
                            ingredientsTextView.setText(ingredientsText);
                            ratingTextView.setText(ratingText);

                            // Creates a intent to WebActivity to show the recipe when clicked
                            findViewById(R.id.buttonWeb).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    finish();
                                    Intent detailIntent = new Intent(RecipeDetailActivity.this, WebActivity.class);
                                    detailIntent.putExtra("source",sourceUrl);
                                    startActivity(detailIntent);


                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        // Add the request to the RequestQueue.
        mRequestQueue.add(request);

    }


    // Method to add a recipe to the users favorite's
    private void addToFavorite(){
        // Gets the info for the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra(EXTRA_ID);
        String urlInput = intent.getStringExtra("imageUrl");
        String recipeInput = intent.getStringExtra("recipe");
        String rating = intent.getStringExtra("rating");

        // Adds the recipe data as class type to Firebase Database
        Recipe recipe = new Recipe(urlInput,recipeInput,input,rating);
        mDatabaseBrancheUser.child(currentUser.getDisplayName()).child(input).setValue(recipe);

        // Shows toast
        Toast.makeText(this,recipeInput +" added to your favorites", Toast.LENGTH_LONG).show();
    }

    // Method to remove a recipe from the users favorite's
    private void removeFromFavorites() {
        Intent intent = getIntent();
        final String input = intent.getStringExtra("id");

        // Creates the right Database Reference
        mDatabaseBrancheUserCheck = mDatabaseBrancheUser.child(currentUser.getDisplayName());

        // Code for a listener that gets the value's from Firebase Database Reference
        mDatabaseBrancheUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Deletes the value with same child as 'input'
                dataSnapshot.child(input).getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });}




    // Method to add a recipe to the group's favorite's
    private void addToGroupFavorites(){
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);


        // Gets the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra("id");
        String urlInput = intent.getStringExtra("imageUrl");
        String recipeInput = intent.getStringExtra("recipe");
        String rating = intent.getStringExtra("rating");

        // Creates the right Database Reference
        DatabaseReference getgroupname = FirebaseDatabase.getInstance().getReference("groupNames");

        if (restoredGroupName != null){
            // Saves the recipe with username and id key to Firebase Database
            Recipe recipe = new Recipe(urlInput,recipeInput,input,rating);
            getgroupname.child(restoredGroupName).child(input).setValue(recipe);

            // Shows toast
            Toast.makeText(this,"Favorite added to the group", Toast.LENGTH_LONG).show();
        }}

    // Method that produces the delete function from grouplist
    private void removeFromGroup() {
        // Creates a new Intent
        Intent intent = getIntent();
        final String input = intent.getStringExtra("id");

        // Receives sharedpreference from other activity
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);

        // Creates the right Database Reference
        mDatabaseBrancheGroupCheck = mDatabaseBrancheGroup.child(restoredGroupName);
        mDatabaseBrancheUserCheck = mDatabaseBrancheUser.child(restoredGroupName);


        // Code for a listener that gets the value's from Firebase Database Reference
        mDatabaseBrancheGroupCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Deletes the value with same child as 'input'
                dataSnapshot.child(input).getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        // Makes a toast appear
        Toast.makeText(this,"Deleted", Toast.LENGTH_LONG).show();

    }

    // Method to check if a recipe is a favorite from the user
    private void checkFavorites(){
        // Gets the id data for the recipe from the list
        Intent intent = getIntent();
        final String input = intent.getStringExtra(EXTRA_ID);

        // Creates the right Database Reference
        mDatabaseBrancheUserCheck = mDatabaseBrancheUser.child(currentUser.getDisplayName());

        // Code for a listener that gets the value's from Firebase Database Reference
        mDatabaseBrancheUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds.getChildren()){
                        System.out.println(ds2);
                        // Checks if the child 'id' is equal to the given id data called 'input'
                        if (ds.child("id").getValue().equals(input)) {
                            // Sets the 'heart' visble if its equal
                            findViewById(R.id.imageView6).setVisibility(View.VISIBLE);
                        }
                        else {
                            // Sets the 'heart' invisible if its not equal
                            findViewById(R.id.imageView6).setVisibility(View.INVISIBLE);
                        }
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
        Intent intent = getIntent();
        String favoriteActivity = intent.getStringExtra("FavoriteActivity");
        String groupActivity = intent.getStringExtra("GroupActivity");

        // Checks if the intent came from FavoriteList Activity, if so; go back to FavoriteList
        if (favoriteActivity != null) {
            startActivity(new Intent(RecipeDetailActivity.this, FavoriteList.class));
        }
        // Checks if the intent came from GroupList Activity, if so; go back to GroupList
        if (groupActivity != null) {
            startActivity(new Intent(RecipeDetailActivity.this, GroupList.class));
        }
    }
}