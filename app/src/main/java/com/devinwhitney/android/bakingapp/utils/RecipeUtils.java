package com.devinwhitney.android.bakingapp.utils;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Recipe;
import com.devinwhitney.android.bakingapp.model.Steps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by devin on 7/21/2018.
 */

public class RecipeUtils {

    public static ArrayList<Recipe> getRecipes() throws IOException {
        URL url = null;
        try {
            url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        StringBuilder buff = new StringBuilder();
        assert url != null;
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scan = new Scanner(in);
            scan.useDelimiter("\\A");

            boolean hasMore = scan.hasNext();
            if (hasMore) {
                buff.append(scan.next());
            }
        } finally {
            urlConnection.disconnect();
        }
        return getRecipeInfoFromJson(buff.toString());
    }

    private static ArrayList<Recipe> getRecipeInfoFromJson(String s) {
        ArrayList<Recipe> allRecipes = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(s);
            int size = jsonArray.length();
            for (int x = 0; x < size; x++) {
                JSONObject jsonObject = jsonArray.getJSONObject(x);
                Recipe recipe = new Recipe();
                recipe.setId((Integer) jsonObject.get("id"));
                recipe.setName((String) jsonObject.get("name"));
                recipe.setServings((Integer) jsonObject.get("servings"));
                recipe.setImages((String) jsonObject.get("image"));
                JSONArray ingredients = jsonObject.getJSONArray("ingredients");
                ArrayList<Ingredients> allIngredients = new ArrayList<>();
                for (int item = 0; item < ingredients.length(); item++) {
                    Ingredients ingredientsObj = new Ingredients();
                    JSONObject ingredientObject = ingredients.getJSONObject(item);
                    Object quantity = ingredientObject.get("quantity");
                    ingredientsObj.setQuantity(String.valueOf(quantity));
                    ingredientsObj.setMeasure((String) ingredientObject.get("measure"));
                    ingredientsObj.setIngredient((String) ingredientObject.get("ingredient"));
                    allIngredients.add(ingredientsObj);
                }
                recipe.setIngredients(allIngredients);
                JSONArray steps = jsonObject.getJSONArray("steps");
                ArrayList<Steps> recipeSteps = new ArrayList<>();
                for (int item = 0; item < steps.length(); item++) {
                    Steps step = new Steps();
                    JSONObject stepObject = steps.getJSONObject(item);
                    step.setId(String.valueOf(stepObject.get("id")));
                    step.setShortDescription((String) stepObject.get("shortDescription"));
                    step.setDescription((String) stepObject.get("description"));
                    step.setVideoURL((String)stepObject.get("videoURL"));
                    step.setThumbnailURL((String) stepObject.get("thumbnailURL"));
                    recipeSteps.add(step);
                }
                recipe.setSteps(recipeSteps);
                allRecipes.add(recipe);
            }
            System.out.println("Json object received");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return allRecipes;
    }
}
