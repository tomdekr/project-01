package com.example.tom_d.bro_cook;

/**
 * Created by Tom_D on 1/18/2018.
 */

public class Recipe {
    private String mImageUrl;
    private String mRecipe;


    public Recipe(String imageUrl, String recipe){
        mImageUrl = imageUrl;
        mRecipe = recipe;
    }

    public String getImageUrl(){
        return mImageUrl;
    }

    public String getRecipe(){
        return mRecipe;
    }
}
