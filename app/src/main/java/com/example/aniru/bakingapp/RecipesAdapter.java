package com.example.aniru.bakingapp;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aniru.bakingapp.data.Recipe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aniru on 8/11/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeHolder>  {

    Context mContext;

    private ArrayList<Recipe> mRecipes = new ArrayList<Recipe>();

    private ListItemClickListener mOnClickListener;

    RecipesAdapter(ArrayList<Recipe> recipes, Context context, ListItemClickListener listener){
        mRecipes = recipes;
        mContext = context;
        mOnClickListener = listener;
    }


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        RecipeHolder viewHolder = new RecipeHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecipeHolder holder, int position) {

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mRecipes.size();
    }

    public void setRecipeData(ArrayList<Recipe> recipes, Context context){

        mContext = context;

        mRecipes.clear();

        mRecipes.addAll(recipes);

        notifyDataSetChanged();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        final TextView tv_recipeName;

        final ImageView iv_RecipeThumbnail;

        public RecipeHolder(View itemView) {
            super(itemView);
            tv_recipeName= (TextView) itemView.findViewById(R.id.tv_recipename);

            iv_RecipeThumbnail = (ImageView) itemView.findViewById(R.id.iv_RecipeThumbnail);

            itemView.setOnClickListener(this);
        }

        void bind(int listIndex) {

            String recipe = mRecipes.get(listIndex).getName();
            tv_recipeName.setText(recipe);

            String recipethumbnailURL = mRecipes.get(listIndex).getImage();

            if(!recipethumbnailURL.isEmpty())
            {
                Picasso.with(mContext).load(recipethumbnailURL)
                        .resize(200,200).centerCrop()
                        .error(R.drawable.no_thumbnail)
                        .into(iv_RecipeThumbnail);
            }
            else
            {
                Picasso.with(mContext).load(R.drawable.no_thumbnail)
                        .resize(200,200)
                        .centerCrop()
                        .into(iv_RecipeThumbnail);
            }
        }

        @Override
        public void onClick(View view) {
            // Let MainActivity handle the click
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }
    }
}
