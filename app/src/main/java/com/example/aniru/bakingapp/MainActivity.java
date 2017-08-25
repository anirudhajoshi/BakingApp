package com.example.aniru.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.aniru.bakingapp.data.Recipe;
import com.example.aniru.bakingapp.data.RecipesInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements RecipesFragment.OnRecipeSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Timber.plant(new Timber.DebugTree());
        Timber.tag("MainActivity");

        if( savedInstanceState==null ) {

            RecipesFragment recipesFragment = new RecipesFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipes_list, recipesFragment)
                    .commit();
        }
    }

    @Override
    public void OnRecipeSelected(Recipe selectedRecipe) {

        Intent intent = new Intent(this, RecipeDetailsActivity.class);

        intent.putExtra("SelectedRecipe",selectedRecipe);

        startActivity(intent);
    }
}
