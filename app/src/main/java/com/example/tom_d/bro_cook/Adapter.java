package com.example.tom_d.bro_cook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Tom_D on 1/18/2018.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ExampleViewHolder>{
    private Context mContext;
    private ArrayList<Recipe> mRecipeList;
    private OnItemClickListener mListener;
    private ValueEventListener mLongListener;



    public interface OnItemClickListener {
        void onItemClick(int position);
    }




    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    public Adapter(Context context, ArrayList<Recipe> recipeList){
        mContext = context;
        mRecipeList = recipeList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);


    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {


        Recipe currentRecipe = mRecipeList.get(position);
        if (currentRecipe == null){
        } else {

            String imageUrl = currentRecipe.getImageUrl();
            String recipeName = currentRecipe.getRecipe();
            String rating = currentRecipe.getRating();

            if (imageUrl != null){
                Picasso.with(mContext).load(imageUrl).fit().centerInside().into(holder.mImageView);
            }
            if (recipeName != null){
                holder.mTextViewRecipe.setText(recipeName);
            }
            if (rating != null){
                holder.mTextViewRating.setText(rating);
            }

        }







    }



    @Override
    public int getItemCount() {
        return (mRecipeList.size()-1);
    }

    public class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextViewRecipe;
        public TextView mTextViewRating;

        public ExampleViewHolder(final View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextViewRecipe = itemView.findViewById(R.id.textView);
            mTextViewRating = itemView.findViewById(R.id.rating);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onItemClick(position);
                        }
                    }
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mListener.onItemClick(getAdapterPosition());
                    Toast.makeText(mContext," added to your favorites", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
                }
            });
        }
    }}
