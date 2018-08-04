package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.devinwhitney.android.bakingapp.model.Steps;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devin on 7/27/2018.
 */

public class RecipeDetailActivity extends AppCompatActivity implements RecipeDetailFragment.OnStepClickListener {

    private static final String RECIPE_FRAGMENT = "recipe_fragment";
    private static final String VIDEO_FRAGMENT = "video_fragment";
    private static final String RECIPE_NAME = "recipe_name";
    public static final String STEPS = "steps_extra";
    public static final String STEP_NUM = "step_number";
    public static final String VIDEO_URL = "video";
    public static final String CURRENT_STEP = "current_step";
    public static final String ALL_STEPS = "all_steps";


    private List<Steps> mSteps;
    private Steps mStep;
    private int mStepPosition;
    private String recipeName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(CURRENT_STEP);
            mSteps = savedInstanceState.getParcelableArrayList(ALL_STEPS);
            mStepPosition = savedInstanceState.getInt(STEP_NUM);
            recipeName = savedInstanceState.getString(RECIPE_NAME);
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                getSupportActionBar().hide();
            }

        } else {
            Intent intent = getIntent();
            mSteps = intent.getParcelableArrayListExtra(STEPS);
            recipeName = intent.getStringExtra(RECIPE_NAME);
            this.setTitle(recipeName);
            mStepPosition = intent.getIntExtra(STEP_NUM, 0);
            mStep = mSteps.get(mStepPosition);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, mStep);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail, recipeDetailFragment, RECIPE_FRAGMENT)
                    .commit();

            VideoFragment videoFragment = new VideoFragment();
            Bundle videoBundle = new Bundle();
            videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
            videoFragment.setArguments(videoBundle);
            fragmentManager.beginTransaction()
                    .add(R.id.detail_video_frame, videoFragment, VIDEO_FRAGMENT)
                    .commit();
        }


    }

    @Override
    public void onButtonSelection(String buttonDirection, int stepNum) {
        System.out.println(buttonDirection + " id = " + stepNum);
        if (buttonDirection.equals("next") && stepNum < mSteps.size() - 1){
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            mStep = mSteps.get(++stepNum);
            bundle.putParcelable(STEPS, mStep);
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail, recipeDetailFragment)
                    .commit();
            VideoFragment videoFragment = new VideoFragment();
            Bundle videoBundle = new Bundle();
            videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
            videoFragment.setArguments(videoBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_video_frame, videoFragment)
                    .commit();
        } else if (buttonDirection.equals("previous") && stepNum > 0) {
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            Bundle bundle = new Bundle();
            mStep = mSteps.get(--stepNum);
            bundle.putParcelable(STEPS, mStep);
            recipeDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail, recipeDetailFragment)
                    .commit();
            VideoFragment videoFragment = new VideoFragment();
            Bundle videoBundle = new Bundle();
            videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
            videoFragment.setArguments(videoBundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_video_frame, videoFragment)
                    .commit();
        } else {
            Toast.makeText(this,"No more steps this direction!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Steps steps = mStep;
        outState.putParcelable(CURRENT_STEP, steps);
        outState.putParcelableArrayList(ALL_STEPS, (ArrayList<? extends Parcelable>) mSteps);
        outState.putInt(STEP_NUM, mStepPosition);
        outState.putString(RECIPE_NAME, recipeName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroyed!");
    }
}
