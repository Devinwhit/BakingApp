package com.devinwhitney.android.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Recipe;
import com.devinwhitney.android.bakingapp.model.Steps;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intending;
import static android.support.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by devin on 8/4/2018.
 */

@RunWith(AndroidJUnit4.class)
public class LaunchRecipeActivityTest {

    private Recipe recipe;

    @Rule
    public IntentsTestRule<MainActivity> mActivityRule = new IntentsTestRule<>(MainActivity.class);
    @Before
    public void populateRecipe(){
        recipe = new Recipe();
        List<Ingredients> ingredients = new ArrayList<>();
        Ingredients ingredient = new Ingredients();
        ingredient.setIngredient("Fake ingredient");
        ingredient.setMeasure("Fake Measurement");
        ingredient.setQuantity("Fake Quantity");
        ingredients.add(ingredient);

        List<Steps> steps = new ArrayList<>();
        Steps step = new Steps();
        step.setThumbnailURL("");
        step.setDescription("Fake Description");
        step.setShortDescription("Fake Short Description");
        step.setId("100");
        step.setVideoURL("Fake video URL");
        steps.add(step);

        recipe.setName("Fake Recipe");
        recipe.setId(100);
        recipe.setImages("");
        recipe.setServings(5);
        recipe.setIngredients(ingredients);
        recipe.setSteps(steps);
    }
    @Before
    public void stubAllExternalIntents(){
        Intent intent = new Intent();
        intent.putExtra("data", recipe);
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }



    @Test
    public void launchActivity() {

        onView(withId(R.id.recipeRV)).perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //check that the network call was successful, thus the text view IS populated with information
        onView(withId(R.id.recipe_ingredients)).check(matches(not(withText(""))));

    }
}
