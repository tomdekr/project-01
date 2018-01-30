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


        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecipeImageActivity.this));

        mRecipeList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);
        parseJSON();

    }

    private void parseJSON() {
//        String url = "http://api.yummly.com/v1/api/recipes?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e&q=potato";

        SharedPreferences settings = RecipeImageActivity.this.getSharedPreferences("input", MODE_PRIVATE);
        final String input = settings.getString("input", "");
        Log.v("RecipeImage Info key", "   " + input); // Log to check spot in branch

        // Makes it able to search the url by the input by user
        String url = ("http://api.yummly.com/v1/api/recipes?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e&q=" + input);

        // Replaces any space in url (input) for underscore to prevent error
        url = url.replace(" ", ", ");




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("matches");


                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject matches = jsonArray.getJSONObject(i);
                                JSONObject img = matches.getJSONObject("imageUrlsBySize");

                                String imageUrl = img.getString("90");
                                Log.v("images", imageUrl);

//                        if (imageUrl != null){
//                            imageUrl = String.valueOf(imageUrl.split("http").toString());
//                        }
                                String recipeName = matches.getString("recipeName");
                                String recipeId = matches.getString("id");
                                String rating = matches.getString("rating");
                                boolean favorite = true;



                                mRecipeList.add(new Recipe(imageUrl,recipeName,recipeId,rating));
                                mExampleAdapter = new Adapter(RecipeImageActivity.this, mRecipeList);
                                mRecyclerView.setAdapter(mExampleAdapter);
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



    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
        Recipe clickedItem = mRecipeList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_CREATOR,clickedItem.getRecipe());
        detailIntent.putExtra(EXTRA_URL,clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_INT, clickedItem.getRating());

        Log.v("id", clickedItem.getId());


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




