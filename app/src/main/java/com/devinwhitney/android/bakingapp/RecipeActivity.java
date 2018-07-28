package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Recipe;
import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 7/24/2018.
 */

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter.RecipeStepAdapterOnClickHandler {

    public static String STEPS = "steps_extra";
    public static String INGREDIENTS = "ingredients_extra";
    public static String STEP_NUM = "step_number";
    List<Steps> steps;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps);
        Intent intent = getIntent();
        Recipe recipe = intent.getParcelableExtra("data");
        steps = recipe.getSteps();
        List<Ingredients> ingredients = recipe.getIngredients();

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) steps);
        bundle.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
        MasterRecipeFragment masterRecipeFragment = new MasterRecipeFragment();
        masterRecipeFragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.recipe_steps, masterRecipeFragment)
                .commit();

    }
/*
    @Override
    public void onButtonSelection(String buttonDirection, int stepNum) {
        if (buttonDirection.equals("next")) {
            stepNum++;
            Steps step = steps.get(stepNum);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, step);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail, recipeDetailFragment)
                    .commit();
        } else {

        }

    }
*/
    @Override
    public void onClick(List<Steps> steps, int position) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putParcelableArrayListExtra(STEPS, (ArrayList<? extends Parcelable>) steps);
        intent.putExtra(STEP_NUM, position);
        startActivity(intent);
    }
}
