package com.example.tom_d.bro_cook;

/**
 * Created by Tom_D on 1/18/2018.
 */

public class Recipe {
    private String mImageUrl;
    private String mRecipe;
    private String mId;
    private String mRating;

    public Recipe(){

    }

    public Recipe(String imageUrl, String recipe, String id, String rating){
        mImageUrl = imageUrl;
        mRecipe = recipe;
        mId = id;
        mRating = rating;
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

    public String getRating(){
        return mRating;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setRecipe(String mRecipe) {
        this.mRecipe = mRecipe;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setRating (String mRating) { this.mRating = mRating;}

}
