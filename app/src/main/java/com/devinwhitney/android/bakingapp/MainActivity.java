package com.devinwhitney.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.devinwhitney.android.bakingapp.model.Recipe;
import com.devinwhitney.android.bakingapp.utils.RecipeUtils;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Recipe>>, RecipeCardAdapter.RecipeCardAdapterOnClickHandler {

    private LinearLayoutManager layoutManager;
    private RecipeCardAdapter mRecipeCardAdapter;
    private ProgressBar mProgressBar;
    private ImageView mRecipeCardImage;

    private static final int RECIPE_LOADER_ID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recipeRV);
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        mRecipeCardAdapter = new RecipeCardAdapter(this);
        recyclerView.setAdapter(mRecipeCardAdapter);

        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mRecipeCardImage = findViewById(R.id.recipeImage);

        LoaderManager.LoaderCallbacks<ArrayList<Recipe>> callback = MainActivity.this;
        Bundle bundle = new Bundle();
        getSupportLoaderManager().initLoader(RECIPE_LOADER_ID, bundle, callback);

    }

    @Override
    public Loader<ArrayList<Recipe>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Recipe>>(this) {
            ArrayList<Recipe> mRecipes = null;
            @Override
            protected void onStartLoading() {
                if (mRecipes != null) {
                    deliverResult(mRecipes);
                } else {
                    forceLoad();
                }

            }

            @Override
            public ArrayList<Recipe> loadInBackground() {
                ArrayList<Recipe> recipes = new ArrayList<>();
                try {
                    recipes = RecipeUtils.getRecipes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return recipes;
            }

            @Override
            public void deliverResult(ArrayList<Recipe> data) {
                mRecipes = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Recipe>> loader, ArrayList<Recipe> data) {
        mRecipeCardAdapter.setRecipes(data);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Recipe>> loader) {

    }


    @Override
    public void onClick(Recipe recipe) {
        System.out.println(recipe.getName());
        Intent intent = new Intent(this, RecipeActivity.class);
        intent.putExtra("data", recipe);
        startActivity(intent);

    }
}
