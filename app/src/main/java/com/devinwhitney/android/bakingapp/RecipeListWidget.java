package com.devinwhitney.android.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.devinwhitney.android.bakingapp.model.Ingredients;
import com.devinwhitney.android.bakingapp.model.Recipe;

import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeListWidget extends AppWidgetProvider {

    public static final String UPDATE_WIDGET = "update_widget";
    private static String mIngredients;
    private static Recipe mRecipes;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        List<Ingredients> ingredients = mRecipes.getIngredients();
        StringBuilder sb = new StringBuilder();
        for (Ingredients ingredient : ingredients) {
            sb.append(ingredient.getQuantity())
                    .append(" ")
                    .append(ingredient.getMeasure())
                    .append(" of ")
                    .append(ingredient.getIngredient())
                    .append("\n");
        }
        mIngredients = sb.toString();

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_list_widget);
        views.setTextViewText(R.id.appwidget_text, mIngredients);

        Intent intent = new Intent(context, RecipeActivity.class);
        intent.putExtra("data", mRecipes);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        if (mRecipes != null) {
            for (int appWidgetId : appWidgetIds) {
                updateAppWidget(context, appWidgetManager, appWidgetId);
            }
        }

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mRecipes = intent.getParcelableExtra(UPDATE_WIDGET);
        super.onReceive(context, intent);

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

