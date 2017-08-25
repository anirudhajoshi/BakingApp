package com.example.aniru.bakingapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.aniru.bakingapp.data.Ingredient;
import com.example.aniru.bakingapp.data.Recipe;
import com.example.aniru.bakingapp.data.RecipesInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by aniru on 8/20/2017.
 */

public class BakingAppWidgetRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private Retrofit retrofit;
    private Gson gson;
    Intent mIntent;

    String mPreferredRecipe;

    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();
    List<Ingredient> mPreferredRecipeIngredients = new ArrayList<Ingredient>();

    public BakingAppWidgetRemoteViewsFactory(Context applicationContext, Intent intent) {
        mContext = applicationContext;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
        mIntent = intent;
    }

    @Override
    public void onCreate() {

        String baseUrl = mContext.getString(R.string.baseURL);
        gson = new GsonBuilder()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Read from SharedPreferences
        SharedPreferences sharedPref = mContext.getSharedPreferences(mContext.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String defaultValue = mContext.getString(R.string.not_found);
        mPreferredRecipe = sharedPref.getString(mContext.getString(R.string.preferredReicpe), defaultValue);

        // Set the containing views preferred recipe textbox so that the user has a visual cue of their preferred recipe
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.bakingapp_widget);
        views.setTextViewText(R.id.tv_preferredRecipeName,mPreferredRecipe);
        AppWidgetManager manager = AppWidgetManager.getInstance(mContext);
        manager.updateAppWidget(mAppWidgetId,views);
    }

    @Override
    public void onDataSetChanged() {

        try {
            // Use retrofit to get the recipes from this URL

            RecipesInterface recipesfromUrl =
                    retrofit.create(RecipesInterface.class);

            Call<ArrayList<Recipe>> call = recipesfromUrl
                    .getRecipes();

            // Call synchronously, i.e. block until the call is completed
            mRecipes = call.execute().body();

            for(int i=0;i<mRecipes.size();i++){
                if( mRecipes.get(i).getName().equalsIgnoreCase(mPreferredRecipe) ){
                    mPreferredRecipeIngredients.addAll(mRecipes.get(i).getIngredients());
                    break;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mPreferredRecipeIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.bakingapp_widget_list_item);

        rv.setTextViewText(R.id.widgetItemIngredientName, mPreferredRecipeIngredients.get(i).getIngredient());
        rv.setTextViewText(R.id.widgetItemIngredientQuantity, String.valueOf(mPreferredRecipeIngredients.get(i).getQuantity()));
        rv.setTextViewText(R.id.widgetItemIngredientMeasure, mPreferredRecipeIngredients.get(i).getMeasure());

        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
