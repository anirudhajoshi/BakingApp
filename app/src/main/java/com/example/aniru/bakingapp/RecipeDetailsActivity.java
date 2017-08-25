package com.example.aniru.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.aniru.bakingapp.data.Recipe;

import timber.log.Timber;

/**
 * Created by aniru on 8/13/2017.
 */

public class RecipeDetailsActivity extends AppCompatActivity implements RecipeDetailsFragment.OnIngredientsAndStepsSelectedListener {

    private Recipe mSelectedRecipe = new Recipe();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipedetails);

        mSelectedRecipe = getIntent().getParcelableExtra("SelectedRecipe");

        Bundle b = new Bundle();
        b.putParcelable("RecipeDetails",mSelectedRecipe);

        if( mSelectedRecipe!=null )
        {
            Timber.d(mSelectedRecipe.toString());

            if( savedInstanceState==null ) {

                if (findViewById(R.id.fragment_container) == null) {
                    RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                    recipeDetailsFragment.setArguments(b);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_details, recipeDetailsFragment)
                            .commit();
                }
                else{
                    RecipeDetailsFragment recipeDetailsFragment = new RecipeDetailsFragment();
                    recipeDetailsFragment.setArguments(b);

                    Bundle b2 = new Bundle();
                    b2.putParcelable("RecipeDetails", mSelectedRecipe);
                    b2.putInt("Position", 0);
                    IngredientsDetails_Fragment IngredientsDetails_fragment = new IngredientsDetails_Fragment();
                    IngredientsDetails_fragment.setArguments(b2);

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .add(R.id.recipe_details, recipeDetailsFragment)
                            .add(R.id.fragment_container, IngredientsDetails_fragment)
                            .commit();
                }
            }
        }
        else
            Timber.d("Recipes is null");
    }

    @Override
    public void OnIngredientsAndStepsSelectedListener(int position) {

        Intent intent = new Intent(this, IngredientsAndStepsActivity.class);
        intent.putExtra("SelectedRecipe",mSelectedRecipe);
        intent.putExtra("SelectedPosition",position);

        if (findViewById(R.id.fragment_container) == null) {
            startActivity(intent);
        }
        else{
            if( position==0 ){
                Bundle b2 = new Bundle();
                b2.putParcelable("RecipeDetails",mSelectedRecipe);
                b2.putInt("Position",0);
                IngredientsDetails_Fragment IngredientsDetails_fragment = new IngredientsDetails_Fragment();
                IngredientsDetails_fragment.setArguments(b2);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, IngredientsDetails_fragment)
                        .commit();
            }
            else {
                Bundle b2 = new Bundle();
                b2.putParcelable("RecipeDetails", mSelectedRecipe);
                b2.putInt("Position", position);
                StepDetails_Fragment stepDetails_Fragment = new StepDetails_Fragment();
                stepDetails_Fragment.setArguments(b2);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, stepDetails_Fragment)
                        .commit();
            }
        }
    }
}
