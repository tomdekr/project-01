package com.example.tom_d.bro_cook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoriteList extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    DatabaseReference mDatabaseFavorites;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<Recipe> favoriteList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String currentUserDisplay = user.getDisplayName();

        mDatabaseFavorites = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/"+currentUserDisplay);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(FavoriteList.this));


        mRecyclerView.setHasFixedSize(true);


        favoriteList = new ArrayList<>();
    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                favoriteList.clear();
                for( DataSnapshot favoriteSnapshot : dataSnapshot.getChildren()){
                    Recipe favorites = favoriteSnapshot.getValue(Recipe.class);
                    favoriteList.add(favorites);
                    Log.v("check hier", favoriteSnapshot.toString());
                }
                mExampleAdapter = new Adapter(FavoriteList.this, favoriteList);
                mRecyclerView.setAdapter(mExampleAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
