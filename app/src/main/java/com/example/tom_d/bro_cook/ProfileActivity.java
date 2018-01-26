package com.example.tom_d.bro_cook;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tom_d.bro_cook.MainActivity;
import com.example.tom_d.bro_cook.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

    EditText editTextUsername;
    EditText editTextGroupname;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Necessary stuff for funcionality
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextGroupname = findViewById(R.id.editTextGroupname);

        progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();

        //This wil load all filled in (working atm) information
        loadUserInformation();

        findViewById(R.id.buttonSaveInfo).setOnClickListener(new View.OnClickListener() {
            // Executes the profile update method called saveUserInformation
            @Override
            public void onClick(View view) {
                saveUserInformation();
                saveGroupName();

            }
        });

        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            // Executes the Logout method
            @Override
            public void onClick(View view) {
                Logout();

            }
        });

        findViewById(R.id.buttonFavorites).setOnClickListener(new View.OnClickListener() {
            // Executes new activity to FavoriteActivity
            @Override
            public void onClick(View view) {

                switch (view.getId()) {
                    case R.id.buttonFavorites:
                        finish();
                        startActivity(new Intent(ProfileActivity.this, FavoriteList.class));
                        break;
                }
            }
        });

}




    // The method that makes it able to log out as an user
    private void Logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


    @Override
    // This makes it able to check if the user is already logged in when opening app after killing it
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        // If the user is not logged in, go to login page
        if (user == null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
    }

    // This is the method to load the updated user information from Firebase
    private void loadUserInformation() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        String restoredGroupName = prefs.getString("groupName", null);
        String groupName = editTextGroupname.getText().toString();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            if (user.getDisplayName() != null) {
                editTextUsername.setText(user.getDisplayName());
            }
            if (restoredGroupName != null){
                editTextGroupname.setText(groupName);
            }

        }
    }

    private void saveGroupName() {
        String displayName = editTextUsername.getText().toString();
        String groupName = editTextGroupname.getText().toString();
        // If input is empty; make noticeable
        if(groupName.isEmpty()){
            editTextGroupname.setError("Name required");
            editTextGroupname.requestFocus();
            return;
        }

        DatabaseReference currentUserDb = FirebaseDatabase.getInstance().getReference().child("userInfo").child(displayName);
        DatabaseReference groupDb = FirebaseDatabase.getInstance().getReference().child("groupNames").child(groupName);
//        // Create for user the info branche called 'Groupname' under the user
        Map firstPost = new HashMap();
        firstPost.put("groupName",groupName);
//
//        // Create a new branche a side from userInfo
//        Map secondPost = new HashMap();
//        secondPost.put("groupName",groupName);
//
        currentUserDb.updateChildren(firstPost);
//        groupDb.setValue(secondPost);

        // Creates a unique key for a group id
        String id = groupDb.push().getKey();

//        Group newGroup = new Group(groupName);
        Map secoondPost = new HashMap();
        secoondPost.put("groupName",groupName);
        groupDb.updateChildren(secoondPost);

        // Save the groupName for RecipeInfoActivity
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        editor.putString("groupName", groupName);
        editor.apply();
        editor.commit();

    }




    // This method makes it able to save the filled in username to the Firebase database
    private void saveUserInformation() {
        String displayName = editTextUsername.getText().toString();

        // If input is empty; make noticeable
        if(displayName.isEmpty()){
            editTextUsername.setError("Name required");
            editTextUsername.requestFocus();
            return;
        }

        // Check for currentuser authentication
        FirebaseUser user = mAuth.getCurrentUser();

        // Creates a request to change the user profile if input for username is not null
        if(user !=null){
            progressBar.setVisibility(View.VISIBLE);
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            // Makes the actual update of the username happen
            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressBar.setVisibility(View.GONE);
                            if(task.isSuccessful()){
                                Toast.makeText(ProfileActivity.this,"Profile is updated!",Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}
