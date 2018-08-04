package com.devinwhitney.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.devinwhitney.android.bakingapp.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by devin on 7/21/2018.
 */

public class RecipeCardAdapter extends RecyclerView.Adapter<RecipeCardAdapter.RecipeAdapterViewHolder> {


    private ArrayList<Recipe> mRecipe = new ArrayList<>();
    private final RecipeCardAdapterOnClickHandler mClickHandler;

    public RecipeCardAdapter(RecipeCardAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public interface RecipeCardAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.recipecard;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int position) {
        System.out.println(position);
        holder.mRecipeTitle.setText(mRecipe.get(position).getName());
        String image = mRecipe.get(position).getImages();
        if (image.equals("")) {
            Picasso.get().load("https://images.pexels.com/photos/459469/pexels-photo-459469.jpeg?cs=srgb&dl=basil-delicious-food-459469.jpg&fm=jpg").into(holder.mRecipeImage);
        }



    }


    @Override
    public int getItemCount() {
        if (null == mRecipe) return 0;
        return mRecipe.size();
    }

    public void setRecipes(ArrayList<Recipe> data) {
        if (data != null) {
            mRecipe = data;
            notifyDataSetChanged();
        } else {
            mRecipe.clear();
        }

    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public final TextView mRecipeTitle;
        public final ImageView mRecipeImage;
        public final CardView mCardView;
        public RecipeAdapterViewHolder(View view) {
            super (view);
            mRecipeTitle = view.findViewById(R.id.info_text);
            mRecipeImage = view.findViewById(R.id.recipeImage);
            mCardView = view.findViewById(R.id.recipe_card_view);
            itemView.setOnClickListener(this);

        }

        public void onClick(View view) {
            int x = getAdapterPosition();
            mClickHandler.onClick(mRecipe.get(x));
        }
    }
}
