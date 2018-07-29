package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
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

    public static String STEPS = "steps_extra";
    public static String STEP_NUM = "step_number";
    public static String VIDEO_URL = "video";
    public static String CURRENT_STEP = "current_step";
    public static String ALL_STEPS = "all_steps";


    private List<Steps> mSteps;
    private Steps mStep;
    private int mStepPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail_activity);
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(CURRENT_STEP);
            mSteps = savedInstanceState.getParcelableArrayList(ALL_STEPS);
            mStepPosition = savedInstanceState.getInt(STEP_NUM);
            int currentOrientation = getResources().getConfiguration().orientation;
            if (currentOrientation == Configuration.ORIENTATION_LANDSCAPE) {
                VideoFragment videoFragment = new VideoFragment();
                Bundle videoBundle = new Bundle();
                videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
                videoFragment.setArguments(videoBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail, videoFragment)
                        .commit();

            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable(STEPS, mStep);
                RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
                recipeDetailFragment.setArguments(bundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.recipe_detail, recipeDetailFragment)
                        .commit();
                VideoFragment videoFragment = new VideoFragment();
                Bundle videoBundle = new Bundle();
                videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
                videoFragment.setArguments(videoBundle);
                fragmentManager.beginTransaction()
                        .replace(R.id.detail_video_frame, videoFragment)
                        .commit();

            }

        } else {
            Intent intent = getIntent();
            mSteps = intent.getParcelableArrayListExtra(STEPS);
            mStepPosition = intent.getIntExtra(STEP_NUM, 0);
            mStep = mSteps.get(mStepPosition);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPS, mStep);
            RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
            recipeDetailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail, recipeDetailFragment)
                    .commit();

            VideoFragment videoFragment = new VideoFragment();
            Bundle videoBundle = new Bundle();
            videoBundle.putString(VIDEO_URL, mStep.getVideoURL());
            videoFragment.setArguments(videoBundle);
            fragmentManager.beginTransaction()
                    .add(R.id.detail_video_frame, videoFragment)
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("Destroyed!");
    }
}
