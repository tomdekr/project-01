package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Displays the RecyclerView with the searchlist of recipes
 */

public class RecipeImageActivity extends AppCompatActivity implements Adapter.OnItemClickListener {
    public static final String EXTRA_ID = "id";
    public static final String EXTRA_CREATOR = "recipe";
    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_INT = "rating";

    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    private RequestQueue mRequestQueue;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_image);

        // Necessary code for functionality
        mRecipeList = new ArrayList<>();

        // Code for making the RecyclerView with certain layout
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecipeImageActivity.this));

        // Creates the volley request
        mRequestQueue = Volley.newRequestQueue(this);

        parseJSON();

    }

    // Method for making the visualisation with the info from the api about any clicked recipe
    private void parseJSON() {
        SharedPreferences settings = RecipeImageActivity.this.getSharedPreferences("input", MODE_PRIVATE);
        final String input = settings.getString("input", "");
        Log.v("RecipeImage Info key", "   " + input); // Log to check spot in branch

        // Makes it able to search the url by the input by user
        String url = ("http://api.yummly.com/v1/api/recipes?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e&q=" + input);

        // Replaces any space in url (input) for underscore to prevent error
        url = url.replace(" ", ", ");

        // Request a JsonObject response from the provided URL.
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Gets the right Json Array to get data from
                            JSONArray jsonArray = response.getJSONArray("matches");
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject matches = jsonArray.getJSONObject(i);
                                JSONObject img = matches.getJSONObject("imageUrlsBySize");

                                String imageUrl = img.getString("90");
                                Log.v("images", imageUrl);

                                // Gets the right value and adds it to the right variable
                                String recipeName = matches.getString("recipeName");
                                String recipeId = matches.getString("id");
                                String rating = "Rating: " + matches.getString("rating");


                                // Adds the values for mRecipeList as class type Recipe
                                mRecipeList.add(new Recipe(imageUrl,recipeName,recipeId,rating));

                                // Makes the mRecipeList from api values visible with the adapter
                                mExampleAdapter = new Adapter(RecipeImageActivity.this, mRecipeList);

                                //Sets the adapter to make the final visualisation for the RecyclerView
                                mRecyclerView.setAdapter(mExampleAdapter);

                                // Sets a on Item Listener for the method OnItemClick
                                mExampleAdapter.setOnItemClickListener(RecipeImageActivity.this);

                            }

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



    // Method that produces the function when an Item in the RecyclerView is clicked
    @Override
    public void onItemClick(int position) {
        // Creates a new Intent
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);

        // Gets the position of the item clicked, to know which values to store in the intent from the right item
        Recipe clickedItem = mRecipeList.get(position);
        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_CREATOR,clickedItem.getRecipe());
        detailIntent.putExtra(EXTRA_URL,clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_INT, clickedItem.getRating());

        // Starts the new Activity
        startActivity(detailIntent);
    }



    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(RecipeImageActivity.this, MainActivity.class));
    }
}




