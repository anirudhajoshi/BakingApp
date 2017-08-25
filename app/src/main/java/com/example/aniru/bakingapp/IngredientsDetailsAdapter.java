package com.example.aniru.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.aniru.bakingapp.data.Ingredient;

import java.util.ArrayList;

/**
 * Created by aniru on 8/15/2017.
 */

public class IngredientsDetailsAdapter extends RecyclerView.Adapter<IngredientsDetailsAdapter.IngredientsDetailsHolder> {

    Context mContext;
    ArrayList<Ingredient> mSelectedRecipe_Ingredients = new ArrayList<Ingredient>();


    public IngredientsDetailsAdapter(ArrayList<Ingredient> ingredients, Context context){
        mSelectedRecipe_Ingredients = ingredients;
        mContext = context;
    }

    @Override
    public IngredientsDetailsAdapter.IngredientsDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.ingredientsdetails_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        IngredientsDetailsAdapter.IngredientsDetailsHolder viewHolder = new IngredientsDetailsAdapter.IngredientsDetailsHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(IngredientsDetailsAdapter.IngredientsDetailsHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {

        return mSelectedRecipe_Ingredients.size();
    }

    public void setSelectedIngredientsData(ArrayList<Ingredient> ingredients, Context context){

        mContext = context;

        mSelectedRecipe_Ingredients = ingredients;

        notifyDataSetChanged();
    }

    public class IngredientsDetailsHolder extends RecyclerView.ViewHolder{

        final TextView tv_IngredientName, tv_IngredientQuantity,
                tv_IngredientMeasure;

        public IngredientsDetailsHolder(View itemView) {
            super(itemView);

            tv_IngredientName = (TextView) itemView.findViewById(R.id.tv_IngredientName);

            tv_IngredientQuantity = (TextView) itemView.findViewById(R.id.tv_IngredientQuantity);

            tv_IngredientMeasure = (TextView) itemView.findViewById(R.id.tv_IngredientMeasure);

        };

        void bind(int listIndex) {

            String ingredientName = String.format( mContext.getString(R.string.IngredientName),
                    mSelectedRecipe_Ingredients.get(listIndex).getIngredient());
            tv_IngredientName.setText(ingredientName);

            String quantity = String.format("%.1f",mSelectedRecipe_Ingredients.get(listIndex).getQuantity());
            String ingredientQuantity = String.format(mContext.getString(R.string.IngredientQuantity),
                    quantity);
            tv_IngredientQuantity.setText(ingredientQuantity);

            String ingredientMeasure = String.format(mContext.getString(R.string.IngredientMeasure),
                    mSelectedRecipe_Ingredients.get(listIndex).getMeasure());
            tv_IngredientMeasure.setText(ingredientMeasure);

        }
    }
}
