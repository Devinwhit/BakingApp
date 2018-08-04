package com.devinwhitney.android.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.List;

/**
 * Created by devin on 7/27/2018.
 */

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder> {

    private final List<Steps> mSteps;
    private final RecipeStepAdapterOnClickHandler mClickHandler;

    public RecipeStepAdapter(RecipeStepAdapterOnClickHandler click, List<Steps> steps) {
        mSteps = steps;
        mClickHandler = click;
    }

    public interface RecipeStepAdapterOnClickHandler {
        void onClick(List<Steps> steps, int position);
    }

    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layout = R.layout.recipe_step_description;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new RecipeStepAdapterViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecipeStepAdapterViewHolder holder, int position) {
        System.out.println(position);
        holder.stepDescription.setText(mSteps.get(position).getDescription());

    }

    @Override
    public int getItemCount() {
        return mSteps.size();
    }

    public class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView stepDescription;

        public RecipeStepAdapterViewHolder(View itemView) {
            super(itemView);
            stepDescription = itemView.findViewById(R.id.recipe_step_description);
            itemView.setOnClickListener(this);
        }

        public void onClick(View view){
            int x = getAdapterPosition();
            mClickHandler.onClick(mSteps, x);
        }
    }

}
