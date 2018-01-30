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

    DatabaseReference databaseBrancheUser;
    DatabaseReference databaseBrancheGroup;
    DatabaseReference databaseBrancheUserCheck;
    DatabaseReference databaseBrancheGroupCheck;
    DatabaseReference drRecipe;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseBrancheUser = FirebaseDatabase.getInstance().getReference("userInfo");
        databaseBrancheGroup = FirebaseDatabase.getInstance().getReference("groupNames");

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
                    // Adds the recipe to favorites if image 'heart' is clicked
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

        Log.v("check dit:", id);
        if (id != null){
            url = url.replace("recipe-id", id);
        }
        if (broId != null){
            url = url.replace("recipe-id", broId);
        }


        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("images");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject matches = jsonArray.getJSONObject(i);
                                String imageUrl = matches.getString("hostedLargeUrl");
                                Log.v("images", imageUrl);


                                ImageView imageView = findViewById(R.id.imageView);
                                Picasso.with(RecipeDetailActivity.this).load(imageUrl).fit().centerInside().into(imageView);
                            }

                            } catch (JSONException e1) {
                            e1.printStackTrace();
                        }


                        try{
                            JSONObject jsonObjectSource = response.getJSONObject("source");

                            String recipe = response.getString("name");
                            final String prepTime = response.getString("totalTime");
                            String ingredients = response.getString("ingredientLines");
                            final String sourceUrl = jsonObjectSource.getString("sourceRecipeUrl");
                            int rating = response.getInt("rating");

                            ingredients = ingredients
                                    .replace("[", "")
                                    .replace("]", "")
                                    .replace("\"","")
                                    .replace(",","\n");

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

        mRequestQueue.add(request);

    }



    private void addToFavorite(){
        // Gets the info for the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra(EXTRA_ID);
        String urlInput = intent.getStringExtra("imageUrl");
        String recipeInput = intent.getStringExtra("recipe");
        String rating = intent.getStringExtra("rating");

        Recipe recipe = new Recipe(urlInput,recipeInput,input,rating);
        databaseBrancheUser.child(currentUser.getDisplayName()).child(input).setValue(recipe);

        // Shows toast
        Toast.makeText(this,recipeInput +" added to your favorites", Toast.LENGTH_LONG).show();
    }

    private void removeFromFavorites() {
        Intent intent = getIntent();
        final String input = intent.getStringExtra("id");

        databaseBrancheUserCheck = databaseBrancheUser.child(currentUser.getDisplayName());
        databaseBrancheUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child(input).getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });}

    private void removeFromGroup() {
        Intent intent = getIntent();
        final String input = intent.getStringExtra("id");

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);

        databaseBrancheGroupCheck = databaseBrancheGroup.child(restoredGroupName);
        databaseBrancheGroupCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.child(input).getRef().removeValue();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
        Toast.makeText(this,"Deleted", Toast.LENGTH_LONG).show();

    }



    private void addToGroupFavorites(){
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);


        // Gets the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra("id");
        String urlInput = intent.getStringExtra("imageUrl");
        String recipeInput = intent.getStringExtra("recipe");
        String rating = intent.getStringExtra("rating");


        // Creates a unique key for a recipe id
//        String id = databaseBrancheGroup.push().getKey();

        DatabaseReference getgroupname = FirebaseDatabase.getInstance().getReference("groupNames");

        if (restoredGroupName != null){
            // Saves the recipe with username and unique key to Firebase Database
            Recipe recipe = new Recipe(urlInput,recipeInput,input,rating);
            getgroupname.child(restoredGroupName).child(input).setValue(recipe);

            // Shows toast
            Toast.makeText(this,"Favorite added to the group", Toast.LENGTH_LONG).show();
        }}

    private void checkFavorites(){
        Intent intent = getIntent();
        final String input = intent.getStringExtra(EXTRA_ID);
        databaseBrancheUserCheck = databaseBrancheUser.child(currentUser.getDisplayName());
        databaseBrancheUserCheck.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds2 : ds.getChildren()){
                    System.out.println(ds2);
                    if (ds.child("id").getValue().equals(input)) {
                        findViewById(R.id.imageView6).setVisibility(View.VISIBLE);
                    }
                    else {
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
        finish();
        startActivity(new Intent(RecipeDetailActivity.this, RecipeImageActivity.class));
    }
}

