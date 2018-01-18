package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class RecipeImageActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_image);


        SharedPreferences settings = RecipeImageActivity.this.getSharedPreferences("input", MODE_PRIVATE);
        final String input = settings.getString("input", "");
        Log.v("RecipeImage Info key", "   " + input); // Log to check spot in branch


        //Thanks to: https://stackoverflow.com/questions/43095334/java-convert-string-to-use-in-url-as-part-of-a-get
        final EditText inputText = (EditText)findViewById(R.id.editTextInput);



        // Instantiate the RequestQueue.
        final RequestQueue queue = Volley.newRequestQueue(this);



    // Makes it able to search the url by the input by user
    String url = ("http://api.yummly.com/v1/api/recipes?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e&q=" + input);

    // Replaces any space in url (input) for underscore to prevent error
    url = url.replace(" ", ", ");

    // Checks if there is a input by user
    if (input.isEmpty()){
        inputText.setError("Input is required!");
        inputText.requestFocus();
        return;
    }

    // Request a string response from the provided URL.
    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
            Request.Method.GET, url, null,
            new Response.Listener<JSONObject>() {

                private JSONObject object = null;
                private JSONArray array = null;

                @Override
                public void onResponse(JSONObject response) {
                    // Searches the api on array called 'matches'
                    try {
                        object = (JSONObject) new JSONObject(response.toString());
                        array = object.getJSONArray("matches");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    ArrayList<String> listdata = new ArrayList<String>();
                    ArrayList<String> listdataid = new ArrayList<String>();

                    try {
                        // Reads the input by user if its not null
                        if (array == null){
                            inputText.setError("Valid input required");
                            inputText.requestFocus();
                            return;
                        }
                        // Checks if the array is not null
                        if (array != null){
                            // For every i in the array get the movie Title
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object2 = array.getJSONObject(i);
                                String recipeName = object2.getString("recipeName");

                                JSONObject object3 = array.getJSONObject(i);
                                String recipeId = object3.getString("id");

                                listdata.add(recipeName);
                                listdataid.add(recipeId);
                            }
                        }}
                    catch (JSONException e) {
                        e.printStackTrace();
                    }


                    // Makes the arraylist from api visible in a row_layout
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.row_layout,
                                    listdata
                            );
                    final ListView mListView = findViewById(R.id.list);

                    //Sets the adapter to make the final visualisation for the listview
                    mListView.setAdapter(adapter);

                    // Makes the arraylist from api visible in a row_layout
                    ArrayAdapter<String> adapterid =
                            new ArrayAdapter<String>(
                                    getApplicationContext(),
                                    R.layout.row_layout,
                                    listdataid
                            );
                    final ListView mListViewId = findViewById(R.id.listId);

                    //Sets the adapter to make the final visualisation for the listview
                    mListViewId.setAdapter(adapterid);

                    // Creates the availability to click on recipes and explains action.
                    try {
                        mListView.setOnItemClickListener(

                                new AdapterView.OnItemClickListener() {

//                                    // Saves the movie title if onItemClick is used
//                                    SharedPreferences settings = RecipeImageActivity.this.getSharedPreferences("movie list", MODE_PRIVATE);
//                                    String item = settings.getString("movie list", "[]");
//
//                                    JSONArray movie_title = new JSONArray(item);



                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        // Shows which item you clicked with toast
                                        String moviePicked = "You selected " +
                                                String.valueOf(mListView.getItemAtPosition(i));
                                        moviePicked = moviePicked.replaceAll("-"," ");
                                        moviePicked = moviePicked.replaceAll("[0-9]", " ");

                                        Toast.makeText(RecipeImageActivity.this, moviePicked, Toast.LENGTH_SHORT).show();

                                        String idPicked = String.valueOf(mListViewId.getItemAtPosition(i));

                                        // If item clicked, go to the item's info page
                                        Intent intent = new Intent(RecipeImageActivity.this, RecipeInfoActivity.class);
                                        intent.putExtra("id", idPicked);
                                        Log.v("intent Check", "   " + idPicked); // Log to check spot in branch

                                        startActivity(intent);
                                    }
                                });
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
        }
    });
    // Add the request to the RequestQueue.
    queue.add(jsonObjectRequest);
}



    // Make sure that when back button is pressed the right activity is displayed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(RecipeImageActivity.this, MainActivity.class));
    }
}




