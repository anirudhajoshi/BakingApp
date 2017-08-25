package com.example.aniru.bakingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.aniru.bakingapp.data.Ingredient;
import com.example.aniru.bakingapp.data.Step;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by aniru on 8/15/2017.
 */

public class StepDetailsAdapter extends RecyclerView.Adapter<StepDetailsAdapter.StepDetailsHolder > {

    Context mContext;
    Step mSelectedStep = new Step();

    public StepDetailsAdapter(Step step, Context context){
        mSelectedStep  = step;
        mContext = context;
    }

    @Override
    public StepDetailsAdapter.StepDetailsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.stepdetails_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        StepDetailsAdapter.StepDetailsHolder viewHolder = new StepDetailsAdapter.StepDetailsHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StepDetailsAdapter.StepDetailsHolder  holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public void setSelectedStepeData(Step step, Context context){

        mContext = context;

        mSelectedStep = step;

        notifyDataSetChanged();
    }

    public class StepDetailsHolder extends RecyclerView.ViewHolder {

        final TextView tv_VideoPlaceHolder, tv_StepDescription;
        final ImageView iv_StepThumbnail;

        public StepDetailsHolder (View itemView) {
            super(itemView);

            tv_VideoPlaceHolder = (TextView) itemView.findViewById(R.id.tv_VideoPlaceHolder);

            tv_StepDescription = (TextView) itemView.findViewById(R.id.tv_StepDescription);

            iv_StepThumbnail = (ImageView) itemView.findViewById(R.id.iv_StepThumbnail);
        }

        void bind(int listIndex) {

            String stepVideoUrl = mSelectedStep.getVideoURL();
            tv_StepDescription.setText(stepVideoUrl);

            String stepDescription = mSelectedStep.getDescription();
            tv_StepDescription.setText(stepDescription);

            String stepthumbnailURL = mSelectedStep.getThumbnailURL();

            if(!stepthumbnailURL.isEmpty())
            {
                Picasso.with(mContext).load(stepthumbnailURL)
                        .resize(200,200).centerCrop()
                        .error(R.drawable.no_thumbnail)
                        .into(iv_StepThumbnail);
            }
            else
            {
                Picasso.with(mContext).load(R.drawable.no_thumbnail)
                        .resize(200,200)
                        .centerCrop()
                        .into(iv_StepThumbnail);
            }
        }
    }
}
