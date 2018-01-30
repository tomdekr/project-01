package com.example.tom_d.bro_cook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GroupList extends AppCompatActivity implements Adapter.OnItemClickListener{
    public static final String EXTRA_ID = "id";
    private RecyclerView mRecyclerView;
    private Adapter mExampleAdapter;
    private ArrayList<Recipe> mRecipeList;
    DatabaseReference mDatabaseFavorites;
    DatabaseReference mDatabaseGroup;
    FirebaseAuth mAuth;
    FirebaseUser user;
    ArrayList<Recipe> favoriteList;
    ArrayList<String>  allTitels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        final String currentUserDisplay = user.getDisplayName();

        mDatabaseFavorites = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/userInfo/" + currentUserDisplay + ("/groupName/"));

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(GroupList.this));


        mRecyclerView.setHasFixedSize(true);


        mRecipeList = new ArrayList<>();
        allTitels = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();


        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            // Listens if there is made a change to the 'favorites' of the current user
            public void onDataChange(DataSnapshot dataSnapshot) {
                String data = null;
                data = dataSnapshot.getValue().toString();

                // Split's the list by ' , ' to make all movie titles with unique stand alone
                String[] lijst = data.split(",");

                // Split's the remaining list for every unique one on ' = ' and adds all values from them to the new arraylist
                for (int i = 0; i < lijst.length; i++) {
                    allTitels.add(lijst[i]);
                }

                String infogroup = allTitels.toString().replace("[", "").replace("]", "");
                Log.v("infoResult", "   " + infogroup); // Log to check result

                mDatabaseGroup = FirebaseDatabase.getInstance().getReferenceFromUrl("https://brocook-6ed95.firebaseio.com/groupNames/" + infogroup + ("/"));


                mDatabaseGroup.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    // Listens if there is made a change to the 'favorites' of the current user
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mRecipeList.clear();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) //--> At this point, ds is an iterator of dataSnapshot; it will iterate the dataSnapshot's children. In this case, the first child's type is String, thus the first iteration of ds will have a type of String.
                        {
                            System.out.println(dataSnapshot.getValue());
                            System.out.println(ds.getValue());

                            String id = (String) ds.child("id").getValue();
                            String imageUrl = (String) ds.child("imageUrl").getValue();
                            String recipe = (String) ds.child("recipe").getValue();//--> at this point, you tried to assign a String to an Object with type "protest" by conversion. This is illegal, so it will throw an exception instead.
                            String rating = (String) ds.child("rating").getValue();

                            mRecipeList.add(new Recipe(imageUrl, recipe, id,rating));

                        }
                        Log.v("yololol", String.valueOf(mRecipeList));

                        mExampleAdapter = new Adapter(GroupList.this, mRecipeList);
                        mRecyclerView.setAdapter(mExampleAdapter);
                        mExampleAdapter.setOnItemClickListener(GroupList.this);

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });}
    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, RecipeDetailActivity.class);
        Recipe clickedItem = mRecipeList.get(position);

        detailIntent.putExtra(EXTRA_ID, clickedItem.getId());
        Log.v("id", clickedItem.getId());


        startActivity(detailIntent);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences settings = GroupList.this.getSharedPreferences("input", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        finish();
        startActivity(new Intent(GroupList.this, ProfileActivity.class));
    }


}




















//
//
//        mDatabaseFavorites.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mRecipeList.clear();
//                for (DataSnapshot ds : dataSnapshot.getChildren()) //--> At this point, ds is an iterator of dataSnapshot; it will iterate the dataSnapshot's children. In this case, the first child's type is String, thus the first iteration of ds will have a type of String.
//                {
//                    System.out.println(dataSnapshot.getValue());
//                    System.out.println(ds.getValue());
//
//                    String id = (String) ds.child("id").getValue();
//                    String imageUrl = (String) ds.child("imageUrl").getValue();
//                    String recipe = (String) ds.child("recipe").getValue();//--> at this point, you tried to assign a String to an Object with type "protest" by conversion. This is illegal, so it will throw an exception instead.
//
//
//                    mRecipeList.add(new Recipe(imageUrl, recipe, id));
//
//
////                    mRecipeList.add(id);
////                    mRecipeList.add(imageUrl);
////                    mRecipeList.add(recipe);
//                }
//                Log.v("yololol", String.valueOf(mRecipeList));
//
//                mExampleAdapter = new Adapter(GroupList.this, mRecipeList);
//                mRecyclerView.setAdapter(mExampleAdapter);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }
//}
