package com.example.aniru.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aniru.bakingapp.data.Recipe;

/**
 * Created by aniru on 8/13/2017.
 */

public class RecipeDetailsAdapter extends RecyclerView.Adapter<RecipeDetailsAdapter.IngredientsAndStepsHolder> {

    Context mContext;
    Recipe mSelectedRecipe;

    private ListItemClickListener mOnClickListener;

    public RecipeDetailsAdapter(Recipe recipe, Context context, ListItemClickListener listener){
        mSelectedRecipe = recipe;
        mContext = context;

        mOnClickListener = listener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public RecipeDetailsAdapter.IngredientsAndStepsHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredientsandsteps_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeDetailsAdapter.IngredientsAndStepsHolder viewHolder = new RecipeDetailsAdapter.IngredientsAndStepsHolder(view);

        return viewHolder;
    }

    public void setSelectedRecipeData(Recipe selectedRecipe, Context context){

        mContext = context;

        mSelectedRecipe = selectedRecipe;

        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(IngredientsAndStepsHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        int iRecipeIngredients = 1;
        int iRecipeSteps = mSelectedRecipe.getSteps().size();
        int iRecipeIngredientsAndSteps = iRecipeIngredients + iRecipeSteps;
        return iRecipeIngredientsAndSteps ;
    }

    public class IngredientsAndStepsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView tv_IngredientAndSteps;

        public IngredientsAndStepsHolder(View itemView) {

            super(itemView);

            tv_IngredientAndSteps = (TextView) itemView.findViewById(R.id.tv_IngredientsAndSteps);

            tv_IngredientAndSteps.setOnClickListener(this);
        }

        void bind(int listIndex) {

            if (listIndex == 0)
                tv_IngredientAndSteps.setText(mContext.getString(R.string.RecipeIngredientChooserText));
            else {
                String msg = String.valueOf(listIndex-1) + mContext.getString(R.string.RecipeStepChooserText);
                tv_IngredientAndSteps.setText(msg);
            }
        }

        @Override
        public void onClick(View view) {
            // Let RecipeDetailsActivity handle the click
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
