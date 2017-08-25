package com.example.aniru.bakingapp;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aniru.bakingapp.data.Ingredient;
import com.example.aniru.bakingapp.data.Recipe;
import com.example.aniru.bakingapp.data.RecipesInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Created by aniru on 8/20/2017.
 */

public class BakingAppWidgetConfigureActivity extends AppCompatActivity implements RecipesAdapter.ListItemClickListener {

    int mBakingAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private Retrofit retrofit;
    private Gson gson;
    ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    @BindView(R.id.rv_preferredrecipe_configwidget)
    RecyclerView rv_preferredrecipe_configwidget;

    @BindView(R.id.tv_configwidget_error)
    TextView tv_configwidget_error;

    private RecipesAdapter mRecipesAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.bakingapp_config_activity);

        Timber.d("BakingAppWidgetConfigureActivity: setResult");

        setResult(RESULT_CANCELED);

        ButterKnife.bind(this, this);

        Timber.d("BakingAppWidgetConfigureActivity: Getting recipes from network");

        //Retrieve intent from WidgetProvider - assign to variable
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mBakingAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            // If the activity was opened with the widget, then set result to canceled to ensure
            // that if user exits the activity, the widget is created and we are not recreating any activity
            Intent intent = new Intent();
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mBakingAppWidgetId);
            setResult(Activity.RESULT_CANCELED, intent);
        }

        rv_preferredrecipe_configwidget.setLayoutManager(new LinearLayoutManager(this));

        // Get recipes from the network to allow user to choose his preferred recipe
        getRecipes();
    }

    private void getRecipes() {

        try {
            String baseUrl = getString(R.string.baseURL);
            gson = new GsonBuilder()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();

            RecipesInterface recipesfromUrl =
                    retrofit.create(RecipesInterface.class);

            Call<ArrayList<Recipe>> call = recipesfromUrl
                    .getRecipes();
            call.enqueue(new Callback<ArrayList<Recipe>>() {

                @Override
                public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {

                    tv_configwidget_error.setVisibility(View.GONE);

                    mRecipes = response.body();
                    Timber.d("BakingAppWidgetConfigureActivity: " + mRecipes.size());

                    InnerRecipesAdapter innerRecipesAdapter = new InnerRecipesAdapter(mRecipes, getApplicationContext());

                    rv_preferredrecipe_configwidget.setAdapter(innerRecipesAdapter);
                }

                @Override
                public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {

                    // We could not retrieve data over the network log the error message
                    tv_configwidget_error.setVisibility(View.VISIBLE);

                    Timber.d( "Network error: " + call.toString() );
                }
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {

    }

    class InnerRecipesAdapter extends RecyclerView.Adapter<InnerRecipesAdapter.RecipeItemsHolder>{

        private ArrayList<Recipe> innerRecipes = new ArrayList<Recipe>();
        Context mContext;

        private InnerRecipesAdapter(ArrayList<Recipe> recipes, Context context) {
            mContext = context;
            innerRecipes = recipes;
        }

        @Override
        public InnerRecipesAdapter.RecipeItemsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            int layoutNeeded = R.layout.bakingapp_config_list_item;
            LayoutInflater inflater = LayoutInflater.from(context);
            boolean shouldAttachToParentImmediately = false;
            View view = inflater.inflate(layoutNeeded, parent, shouldAttachToParentImmediately);

            return new RecipeItemsHolder(view);
        }

        @Override
        public void onBindViewHolder(InnerRecipesAdapter.RecipeItemsHolder holder, int position) {
            holder.bind(position);
        }

        @Override
        public int getItemCount() {
            return innerRecipes.size();
        }

        class RecipeItemsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            @BindView(R.id.tv_configwidget_ingredient_name)
            TextView configwidget_ingredient_name;

            List<Ingredient> mIngredients;
            String mWidgetRecipeTitleChosen;

            private RecipeItemsHolder(View v) {
                super(v);
                ButterKnife.bind(this, v);
                itemView.setOnClickListener(this);
            }

            private void bind(int position) {

                String recipe = innerRecipes.get(position).getName();
                configwidget_ingredient_name.setText(recipe);
            }

            @Override
            public void onClick(View view) {

                int clickedPosition = getAdapterPosition();

                if (mBakingAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {

                    // Prepare the shared preference file
                    SharedPreferences sharedPref = mContext.getSharedPreferences(
                            getString(R.string.preference_file_key), Context.MODE_PRIVATE);

                    // Write to the shared preference file
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.clear();
                    editor.commit();        // Make sure we have cleared previous preference
                    editor.putString(getString(R.string.preferredReicpe), mRecipes.get(clickedPosition).getName());
                    editor.commit();

                    String defaultValue = mContext.getString(R.string.not_found);
                    String preferredRecipe = sharedPref.getString(mContext.getString(R.string.preferredReicpe), defaultValue);

                    Intent intent = new Intent();
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mBakingAppWidgetId);
                    // if operation succeeded, therefore finish this call
                    setResult(Activity.RESULT_OK, intent);
                    finish();

                    // Toast.makeText(mContext, mRecipes.get(clickedPosition).getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
