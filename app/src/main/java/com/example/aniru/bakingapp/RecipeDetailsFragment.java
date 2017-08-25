package com.example.aniru.bakingapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.aniru.bakingapp.data.Recipe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by aniru on 8/14/2017.
 */

public class RecipeDetailsFragment extends Fragment implements RecipeDetailsAdapter.ListItemClickListener {

    private Recipe mSelectedRecipe = new Recipe();

    private RecipeDetailsAdapter mRecipeDetailsAdapter;

    @BindView(R.id.rv_ingredientsandsteps)
    RecyclerView rv_ingredientsandsteps;

    public RecipeDetailsFragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();

        mSelectedRecipe = b.getParcelable("RecipeDetails");

        if( mSelectedRecipe!=null ){
            Timber.d(mSelectedRecipe.toString());
        }
        else{
            Timber.d("Recipe is null");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.recipedetails_fragment,container,false);

        ButterKnife.bind(this, view);

        getActivity().setTitle(mSelectedRecipe.getName());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);

        // Set the layout manager to the gridlayoutmanager
        rv_ingredientsandsteps.setLayoutManager(gridLayoutManager);

        // Make sure recyclerview size does not change when items are added or removed from it
        rv_ingredientsandsteps.setHasFixedSize(true);

        mRecipeDetailsAdapter = new RecipeDetailsAdapter(mSelectedRecipe, getContext(), this);

        mRecipeDetailsAdapter.setSelectedRecipeData(mSelectedRecipe, getContext());

        rv_ingredientsandsteps.setAdapter(mRecipeDetailsAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;

            try {
                mCallback = (RecipeDetailsFragment.OnIngredientsAndStepsSelectedListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString()
                        + " must implement OnIngredientAndStepsSelectedListener");
            }
        }
    }

    OnIngredientsAndStepsSelectedListener mCallback;

    // Container Activity must implement this interface
    public interface OnIngredientsAndStepsSelectedListener {
        public void OnIngredientsAndStepsSelectedListener(int stepPosition);
    }


    @Override
    public void onListItemClick(int clickedItemIndex) {
          // Let RecipeDetailsActivity handle the clicks
          mCallback.OnIngredientsAndStepsSelectedListener(clickedItemIndex);
    }
}
