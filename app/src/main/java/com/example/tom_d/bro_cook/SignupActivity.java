package com.example.tom_d.bro_cook;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

        public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

            ProgressBar progressBar;
            EditText editTextEmail, editTextPassword;
            private FirebaseAuth mAuth;

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_signup);

                //Necessary code for functionality
                editTextEmail = findViewById(R.id.editTextEmail);
                editTextPassword = findViewById(R.id.editTextPassword);
                progressBar = findViewById(R.id.progressbar);
                mAuth = FirebaseAuth.getInstance();
                findViewById(R.id.buttonSignup).setOnClickListener(this);

            }
            //Method to make the user register to Firebase authentication
            private void registerUser(){
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                // Check if email is put in
                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required!");
                    editTextEmail.requestFocus();
                    return;
                }
                // Check if email is valid
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    editTextEmail.setError("Put in valid email please");
                    editTextEmail.requestFocus();
                    return;
                }
                // Check if password is empty
                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required!");
                    editTextPassword.requestFocus();
                    return;
                }
                // Check if password is more than 5 char
                if (password.length()<6){
                    editTextEmail.setError("Need more characters than 5");
                    editTextEmail.requestFocus();
                    return;
                }
                // Show progress
                progressBar.setVisibility(View.VISIBLE);

                //Makes the user able to register with the authentication by Firebase
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    finish();
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(getApplicationContext(), "User registered successfull", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class ));
                                }else{
                                    // Show that email is already registered, if so.
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException){
                                        Toast.makeText(getApplicationContext(), "Email is already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Show that user register is failed
                                        Toast.makeText(getApplicationContext(), "User registered failed", Toast.LENGTH_SHORT).show();
                                    }
                                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.buttonSignup:
                        // Go to page to register user
                        registerUser();
                        break;

                }
            }
        }




