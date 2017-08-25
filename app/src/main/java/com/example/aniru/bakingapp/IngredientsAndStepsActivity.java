package com.example.aniru.bakingapp;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.aniru.bakingapp.data.Recipe;

import timber.log.Timber;

/**
 * Created by aniru on 8/15/2017.
 */

public class IngredientsAndStepsActivity extends AppCompatActivity {

    private Recipe mSelectedRecipe = new Recipe();
    int mPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_ingredientsandsteps);

        // mSelectedRecipe = getIntent().getParcelableExtra("SelectedRecipe");

        mSelectedRecipe = getIntent().getParcelableExtra("SelectedRecipe");
        mPosition = getIntent().getIntExtra("SelectedPosition",-1);

        Bundle b = new Bundle();
        b.putParcelable("RecipeDetails",mSelectedRecipe);
        b.putInt("Position",mPosition);

        if( mSelectedRecipe!=null )
        {
            Timber.d(mSelectedRecipe.toString());

            if( savedInstanceState==null ) {

                FragmentManager fragmentManager = getSupportFragmentManager();

                if( mPosition==0 ){
                    // Toast.makeText(this, mSelectedRecipe.getIngredients().get(0).getIngredient(), Toast.LENGTH_SHORT).show();

                    IngredientsDetails_Fragment IngredientsDetails_fragment = new IngredientsDetails_Fragment();
                    IngredientsDetails_fragment.setArguments(b);

                    fragmentManager.beginTransaction()
                            .add(R.id.ingredientandsteps_details, IngredientsDetails_fragment)
                            .commit();
                }
                else if( mPosition>0 ){
                    StepDetails_Fragment stepDetails_fragment = new StepDetails_Fragment();
                    stepDetails_fragment.setArguments(b);

                    // Toast.makeText(this, step, Toast.LENGTH_SHORT).show();
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredientandsteps_details, stepDetails_fragment)
                            .commit();
                }
                else
                    Timber.d("Invalid position. Position is -1");
            }
        }
        else
            Timber.d("Recipes is null");
    }
}
