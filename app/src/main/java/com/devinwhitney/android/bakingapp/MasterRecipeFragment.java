package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 7/27/2018.
 */

public class MasterRecipeFragment extends Fragment {

    public static String STEPS = "steps_extra";
    public static String INGREDIENTS = "ingredients_extra";

    public MasterRecipeFragment() {
        //empty constructor
    }

    List<Steps> mSteps;
    List<Ingredients> mIngredientList;
    private TextView mIngredients;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSteps = savedInstanceState.getParcelableArrayList(STEPS);
            mIngredientList = savedInstanceState.getParcelableArrayList(INGREDIENTS);
        } else {
            mSteps = getArguments().getParcelableArrayList(STEPS);
            mIngredientList = getArguments().getParcelableArrayList(INGREDIENTS);
        }

        final View view = inflater.inflate(R.layout.view_recipes, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recipe_step_RV);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecipeStepAdapter recipeStepAdapter = new RecipeStepAdapter((RecipeStepAdapter.RecipeStepAdapterOnClickHandler) getContext(), mSteps);
        //recipeStepAdapter.setSteps(mSteps);

        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(recipeStepAdapter);
        mIngredients = view.findViewById(R.id.recipe_ingredients);
        StringBuilder sb = new StringBuilder();
        for (Ingredients ingredient : mIngredientList) {
            sb.append(ingredient.getQuantity())
                .append(" ")
                .append(ingredient.getMeasure())
                .append(" of ")
                .append(ingredient.getIngredient())
                .append("\n");
        }
        mIngredients.setText(sb.toString());

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) mSteps);
        outState.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) mIngredientList);
    }
}
