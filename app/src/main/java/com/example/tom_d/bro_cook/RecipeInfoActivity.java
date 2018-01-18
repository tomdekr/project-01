package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.TextureView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeInfoActivity extends AppCompatActivity {

    DatabaseReference databaseBrancheUser;
    DatabaseReference databaseBrancheGroup;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;


    public ArrayList<String> list = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_info);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);

        final ListView mListView = (ListView) findViewById(R.id.list);

        Intent intent = getIntent();
        String input = intent.getStringExtra("id");
        Log.v("Vierde key", "   " + input); // Log to check spot in branch

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        databaseBrancheUser = FirebaseDatabase.getInstance().getReference("userInfo");
        databaseBrancheGroup = FirebaseDatabase.getInstance().getReference("groupInfo");


//        SharedPreferences settings = this.getSharedPreferences("movie title", MODE_PRIVATE);
//        String item = settings.getString("movie title", "");
//        final String input = settings.getString("movie title", "");
        final TextView text = (TextView) findViewById(R.id.text);

        findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adds the movie to favorites if image 'heart' is clicked
                addToFavorite();
            }
        });

        findViewById(R.id.buttonGroup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Adds the movie to favorites if image 'heart' is clicked
                addToGroupFavorites();
            }
        });


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        String url = ("http://api.yummly.com/v1/api/recipe/recipe-id?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e");

        // Replaces any space in url (input) for underscore to prevent error
        url = url.replace("recipe-id", String.valueOf(input));



        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject newObject = (JSONObject) new JSONObject(response);


                            String Name = newObject.getString("name");
                            String preparationTime = newObject.getString("totalTime");
                            //String cookTime = newObject.getString("cookTime");
                            String ingredients = newObject.getString("ingredientLines");
                            String recipeLink = newObject.getString("source");

                            ingredients = ingredients.replaceAll("\"","");


                            list.add(Name);
                            list.add("Prep Time: " + preparationTime);
                            //list.add("Cook Time: " + cookTime);

                            list.add("Ingredients: " + ingredients );






//                            String rating = newObject.getString("rating");
//                            ("Rating: " + rating );










                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        // Makes the arraylist from api visible in a row_layout
                        ArrayAdapter<String> adapter =
                                new ArrayAdapter<String>(
                                        getApplicationContext(),
                                        R.layout.row_layout,
                                        list
                                );
                        ListView mListView = findViewById(R.id.list);
                        // Sets the adapter to make the final visualisation for the listview
                        mListView.setAdapter(adapter);

                        // Makes the listview item's clickable
                        try {
                            mListView.setOnItemClickListener(
                                    new AdapterView.OnItemClickListener() {
                                        @Override
                                        // Shows how to add the movie to favorites
                                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                            String moviePicked = "Click on the heart to set the movie to your favorites";
                                            Toast.makeText(RecipeInfoActivity.this, moviePicked, Toast.LENGTH_LONG).show();
                                        }

                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }}
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    // Method that is attached to the 'add' button to add the movie from InfoActivity to favorites
//    private void addToFavorite(){
//        // Gets the recipe from the list
//        Intent intent = getIntent();
//        String input = intent.getStringExtra("id");
//
//        // Creates a unique key for a recipe id
//        String id = databaseBrancheUser.push().getKey();
//
//        // Saves the recipe with username and unique key to Firebase Database
//        databaseBrancheUser.child(currentUser.getDisplayName()).child("recipeInfo").child(id).setValue(input);
//
//        // Shows toast
//        Toast.makeText(this,"Favorite added to your favorites", Toast.LENGTH_LONG).show();
//    }

    private void addToFavorite(){
        // Gets the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra("id");


        // Creates a unique key for a recipe id
        String id = databaseBrancheUser.push().getKey();

        User newUser = new User(input);

        // Saves the recipe with username and unique key to Firebase Database
        databaseBrancheUser.child(currentUser.getDisplayName()).child(id).setValue(input);

        // Shows toast
        Toast.makeText(this,"Favorite added to your favorites", Toast.LENGTH_LONG).show();
    }

    private void addToGroupFavorites(){
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);


        // Gets the recipe from the list
        Intent intent = getIntent();
        String input = intent.getStringExtra("id");


        // Creates a unique key for a recipe id
        String id = databaseBrancheGroup.push().getKey();

        DatabaseReference getgroupname = FirebaseDatabase.getInstance().getReference("groupNames");

        if (restoredGroupName != null){
        // Saves the recipe with username and unique key to Firebase Database
        getgroupname.child(restoredGroupName).child(id).setValue(input);
        // Shows toast
        Toast.makeText(this,"Favorite added to the group", Toast.LENGTH_LONG).show();
    }}

    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(RecipeInfoActivity.this, RecipeImageActivity.class));
    }
}

