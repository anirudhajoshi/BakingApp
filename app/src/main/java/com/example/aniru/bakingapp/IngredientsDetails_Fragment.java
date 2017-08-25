package com.example.aniru.bakingapp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by aniru on 8/15/2017.
 */
import com.example.aniru.bakingapp.data.Ingredient;
import com.example.aniru.bakingapp.data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

public class IngredientsDetails_Fragment extends Fragment {

    private Recipe mSelectedRecipe = new Recipe();
    private int mPosition = -1;

    @BindView(R.id.rv_ingredients_details)
    RecyclerView rv_ingredients_details;

    private IngredientsDetailsAdapter mIngredientsAndStepsDetailsAdapter;

    public IngredientsDetails_Fragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();

        mSelectedRecipe = b.getParcelable("RecipeDetails");
        mPosition = b.getInt("Position");

        if( mSelectedRecipe!=null && mPosition!=-1){
            Timber.d(mSelectedRecipe.toString());

        }
        else{
            Timber.d("Recipe is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.ingredientsdetails_fragment,container,false);

        ButterKnife.bind(this, view);

        getActivity().setTitle(mSelectedRecipe.getName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // Set the layout manager
        rv_ingredients_details.setLayoutManager(linearLayoutManager);

        // Make sure recyclerview size does not change when items are added or removed from it
        rv_ingredients_details.setHasFixedSize(true);

        mIngredientsAndStepsDetailsAdapter = new IngredientsDetailsAdapter((ArrayList<Ingredient>) mSelectedRecipe.getIngredients(), getContext());

        mIngredientsAndStepsDetailsAdapter.setSelectedIngredientsData((ArrayList<Ingredient>) mSelectedRecipe.getIngredients(), getContext());

        rv_ingredients_details.setAdapter(mIngredientsAndStepsDetailsAdapter);

        return view;
    }
}
