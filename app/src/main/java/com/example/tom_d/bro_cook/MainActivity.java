package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.example.tom_d.bro_cook.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Instantiate the RequestQueue.

        // Does the 'Search' function on api when clicked on this button
        findViewById(R.id.buttonGET).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //Thanks to: https://stackoverflow.com/questions/43095334/java-convert-string-to-use-in-url-as-part-of-a-get
            final EditText inputText = (EditText) findViewById(R.id.editTextInput);
            final String input = inputText.getText().toString();
            Log.v("Input Check", "   " + input); // Log to check spot in branch

            // Makes it able to search the url by the input by user
            String url = ("http://api.yummly.com/v1/api/recipes?_app_id=d77dc66c&_app_key=e03fc2ee7af2a8271e7200e35155104e&q=" + input);

            // Replaces any space in url (input) for underscore to prevent error
            url = url.replace(" ", ", ");

            // Checks if there is a input by user
            if (input.isEmpty()) {
                inputText.setError("Input is required!");
                inputText.requestFocus();
                return;
            }

            // Saves the 'input' into shared pref
            SharedPreferences settings = MainActivity.this.getSharedPreferences("input", MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("input", input);
            editor.commit();
            editor.apply();

            switch (view.getId()) {
                case R.id.buttonGET:
                    finish();
                    startActivity(new Intent(MainActivity.this, RecipeImageActivity.class));
                    break;
            }
        }

    });
    }




    public void goToActivity(View view) {
        switch (view.getId()) {
            case R.id.buttonProfile:
                finish();
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
        }
        switch (view.getId()) {
            case R.id.buttonGroup:
                finish();
                startActivity(new Intent(MainActivity.this, GroupActivity.class));
                break;
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences settings = MainActivity.this.getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        finish();
        startActivity(new Intent(MainActivity.this, MainActivity.class));
    }
}




