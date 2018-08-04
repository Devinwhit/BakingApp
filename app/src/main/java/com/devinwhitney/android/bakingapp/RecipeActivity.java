package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IntegerRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Recipe;
import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

import static com.devinwhitney.android.bakingapp.RecipeDetailActivity.ALL_STEPS;
import static com.devinwhitney.android.bakingapp.RecipeDetailActivity.CURRENT_STEP;
import static com.devinwhitney.android.bakingapp.RecipeDetailActivity.VIDEO_URL;

/**
 * Created by devin on 7/24/2018.
 */

public class RecipeActivity extends AppCompatActivity implements RecipeStepAdapter.RecipeStepAdapterOnClickHandler {

    private static final String RECIPE = "recipe_extra";
    public static String STEPS = "steps_extra";
    public static String INGREDIENTS = "ingredients_extra";
    public static String STEP_NUM = "step_number";
    public static String ALL_STEPS = "all_steps";
    private static String TWO_PANE = "two_pane";

    private Recipe mRecipe;
    private List<Steps> mSteps;
    private Steps mStep;
    private int mStepPosition;
    private boolean mTwoPane;
    private List<Ingredients> ingredients;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_steps);
        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(RECIPE);
            mSteps = savedInstanceState.getParcelableArrayList(ALL_STEPS);
            ingredients = savedInstanceState.getParcelable(INGREDIENTS);
            mTwoPane = savedInstanceState.getBoolean(TWO_PANE);
        } else {
            Intent intent = getIntent();
            mRecipe = intent.getParcelableExtra("data");
            mSteps = mRecipe.getSteps();
            ingredients = mRecipe.getIngredients();
            if (findViewById(R.id.detail_wide_screen) != null) {
                mTwoPane = true;

                Bundle allStepsBundle = new Bundle();
                allStepsBundle.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>) mSteps);
                allStepsBundle.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);

                FragmentManager fragmentManager = getSupportFragmentManager();

                MasterRecipeFragment masterRecipeFragment = new MasterRecipeFragment();
                masterRecipeFragment.setArguments(allStepsBundle);

                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                Bundle recipeBundle = new Bundle();
                recipeBundle.putParcelable(STEPS, mSteps.get(0));
                recipeDetailFragment.setArguments(recipeBundle);

                Bundle videoBundle = new Bundle();
                videoBundle.putString(VIDEO_URL, mSteps.get(0).getVideoURL());
                VideoFragment videoFragment = new VideoFragment();
                videoFragment.setArguments(videoBundle);

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps, masterRecipeFragment)
                        .commit();

                fragmentManager.beginTransaction()
                        .add(R.id.detail_video_frame, videoFragment)
                        .commit();

                fragmentManager.beginTransaction()
                        .add(R.id.detail_wide_screen, recipeDetailFragment)
                        .commit();


            } else{

                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList(STEPS, (ArrayList<? extends Parcelable>)mSteps);
                bundle.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
                MasterRecipeFragment masterRecipeFragment = new MasterRecipeFragment();
                masterRecipeFragment.setArguments(bundle);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.recipe_steps, masterRecipeFragment)
                        .commit();
            }

        }





    }

    @Override
    public void onClick(List<Steps> steps, int position) {
        Steps step = steps.get(position);
        mStep = step;
        if (mTwoPane) {
            System.out.println("Two pane ingredient selected!");
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, step);
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_wide_screen, recipeDetailFragment)
                    .commit();
            VideoFragment videoFragment = new VideoFragment();
            Bundle videoBundle = new Bundle();
            videoBundle.putString(VIDEO_URL, step.getVideoURL());
            videoFragment.setArguments(videoBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_video_frame, videoFragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putParcelableArrayListExtra(STEPS, (ArrayList<? extends Parcelable>) steps);
            intent.putExtra(STEP_NUM, position);
            startActivity(intent);

        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Steps steps = mStep;
        outState.putParcelable(CURRENT_STEP, steps);
        outState.putParcelableArrayList(ALL_STEPS, (ArrayList<? extends Parcelable>) mSteps);
        outState.putInt(STEP_NUM, mStepPosition);
        outState.putParcelable(RECIPE, mRecipe);
        outState.putParcelableArrayList(INGREDIENTS, (ArrayList<? extends Parcelable>) ingredients);
        outState.putBoolean(TWO_PANE, mTwoPane);
    }
}
