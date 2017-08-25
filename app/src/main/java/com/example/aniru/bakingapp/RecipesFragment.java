package com.example.aniru.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Point;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

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

/**
 * Created by aniru on 8/11/2017.
 */

public class RecipesFragment extends Fragment implements RecipesAdapter.ListItemClickListener{

    private final Gson gson = new GsonBuilder()
            .create();

    private Retrofit retrofit;

    private ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    private RecipesAdapter mRecipesAdapter;

    @BindView(R.id.rv_recipesname)
    RecyclerView rv_recipesname;

    @BindView(R.id.tv_errormsg)
    TextView tv_errormsg;

    public RecipesFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.recipes_fragment,container,false);

        ButterKnife.bind(this, view);

        GridLayoutManager gridLayoutManager;
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (tabletSize) {
            if( getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
                // Tablet in landscape mode - stack cells horizontally
                gridLayoutManager = new GridLayoutManager(getContext(), 4);
            else
                // Tablet in portrait mode - stack cells vertically
                gridLayoutManager = new GridLayoutManager(getContext(), 1);
        } else {
            // Phone in landscape or portrait modes stacks cells vertically
            gridLayoutManager = new GridLayoutManager(getContext(), 1);
        }

        // Set the layout manager to the gridlayoutmanager
        rv_recipesname.setLayoutManager(gridLayoutManager);

        // Make sure recyclerview size does not change when items are added or removed from it
        rv_recipesname.setHasFixedSize(true);

        mRecipesAdapter = new RecipesAdapter(mRecipes, getContext(), this);

        if( savedInstanceState==null ) {
            // Fetch the recipes from the internet
            FetchRecipesFromUrl();
        }
        else {

            mRecipes = savedInstanceState.getParcelableArrayList("Recipes");

            mRecipesAdapter.setRecipeData(mRecipes,getContext());

            rv_recipesname.setAdapter(mRecipesAdapter);
        }

        return view;
    }

    private void FetchRecipesFromUrl() {

        // https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json
        String baseUrl = getString(R.string.baseURL);
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Use retrofit to get the recipes from this URL
        RecipesInterface recipesfromUrl =
                retrofit.create(RecipesInterface.class);

        Call<ArrayList<Recipe>> call = recipesfromUrl
                .getRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {

            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                tv_errormsg.setVisibility(View.INVISIBLE);

                mRecipes = response.body();

                mRecipesAdapter.setRecipeData(mRecipes,getContext());

                rv_recipesname.setAdapter(mRecipesAdapter);
            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

                // We could not retrieve data over the network so show the error message
                tv_errormsg.setVisibility(View.VISIBLE);
                tv_errormsg.setText(getString(R.string.errormsg));

                Timber.d( "Network error: " + call.toString() );
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelableArrayList("Recipes", (ArrayList<Recipe>) mRecipes);

        Timber.d("Saved the list");
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        // Toast.makeText(getContext(),mRecipes.get(clickedItemIndex).getName(), Toast.LENGTH_SHORT).show();

        Recipe r = mRecipes.get(clickedItemIndex);

        mCallback.OnRecipeSelected(r);
    }

    OnRecipeSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnRecipeSelectedListener {
        public void OnRecipeSelected(Recipe selectedRecipe);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;

            try {
                mCallback = (OnRecipeSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnRecipeSelectedListener");
            }
        }

    }
}
