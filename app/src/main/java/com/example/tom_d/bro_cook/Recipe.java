package com.example.tom_d.bro_cook;

/**
 * Created by Tom_D on 1/18/2018.
 */

public class Recipe {
    private String mImageUrl;
    private String mRecipe;
    private String mId;


    public Recipe(String imageUrl, String recipe, String id){
        mImageUrl = imageUrl;
        mRecipe = recipe;
        mId = id;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public String getRecipe(){
        return mRecipe;
    }

    public String getId(){
        return mId;
    }

}
