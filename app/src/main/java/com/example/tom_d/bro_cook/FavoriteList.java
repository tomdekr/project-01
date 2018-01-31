package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_CREATOR;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_INT;
import static com.example.tom_d.bro_cook.RecipeImageActivity.EXTRA_URL;

public class FavoriteList extends AppCompatActivity implements Adapter.OnItemClickListener {
    public static final String EXTRA_ID = "id";
    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    DatabaseReference mDatabaseFavorites;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String currentUserDisplay = user.getDisplayName();

        mDatabaseFavorites = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/"+currentUserDisplay+"/");


        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(FavoriteList.this));

        mRecipeList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();

        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mRecipeList.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) //--> At this point, ds is an iterator of dataSnapshot; it will iterate the dataSnapshot's children. In this case, the first child's type is String, thus the first iteration of ds will have a type of String.
                {
                    String id = (String) ds.child("id").getValue();
                    String imageUrl = (String) ds.child("imageUrl").getValue();
                    String recipe = (String) ds.child("recipe").getValue();//--> at this point, you tried to assign a String to an Object with type "protest" by conversion. This is illegal, so it will throw an exception instead.
                    String rating = (String) ds.child("rating").getValue();

                    mRecipeList.add(new Recipe(imageUrl, recipe, id, rating));


                }

                mExampleAdapter = new Adapter(FavoriteList.this, mRecipeList);

                mRecyclerView.setAdapter(mExampleAdapter);
                mExampleAdapter.setOnItemClickListener(FavoriteList.this);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
        Recipe clickedItem = mRecipeList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        detailIntent.putExtra(EXTRA_URL, clickedItem.getImageUrl());
        detailIntent.putExtra(EXTRA_CREATOR, clickedItem.getRecipe());
        detailIntent.putExtra(EXTRA_INT, clickedItem.getRating());


        Log.v("id", clickedItem.getId());


        startActivity(detailIntent);
    }


}
