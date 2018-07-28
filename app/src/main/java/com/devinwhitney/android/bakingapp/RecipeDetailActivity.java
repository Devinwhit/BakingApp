package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.List;

/**
 * Created by devin on 7/27/2018.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    public static String STEPS = "steps_extra";
    public static String STEP_NUM = "step_number";

    private List<Steps> mSteps;
    private int mStepPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        Intent intent = getIntent();
        mSteps = intent.getParcelableArrayListExtra(STEPS);
        mStepPosition = intent.getIntExtra(STEP_NUM, 0);
        Bundle bundle = new Bundle();
        bundle.putParcelable(STEPS, mSteps.get(mStepPosition));
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        recipeDetailFragment.setArguments(bundle);
        fragmentManager.beginTransaction()
                .add(R.id.recipe_detail, recipeDetailFragment)
                .commit();
    }

    @Override
    public void onButtonSelection(String buttonDirection, int stepNum) {
        System.out.println(buttonDirection + " id = " + stepNum);
        if (buttonDirection.equals("next") && stepNum < mSteps.size() - 1){
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, mSteps.get(++stepNum));
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail, recipeDetailFragment)
                    .commit();
        } else if (buttonDirection.equals("previous") && stepNum > 0) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, mSteps.get(--stepNum));
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail, recipeDetailFragment)
                    .commit();
        } else {
            Toast.makeText(this,"No more steps this direction!", Toast.LENGTH_SHORT).show();
        }

    }
}
