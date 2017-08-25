package com.example.aniru.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.aniru.bakingapp.data.Ingredient;
import com.example.aniru.bakingapp.data.Recipe;
import com.example.aniru.bakingapp.data.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;



/**
 * Created by aniru on 8/15/2017.
 */

public class StepDetails_Fragment extends Fragment implements ExoPlayer.EventListener{
    private Recipe mSelectedRecipe = new Recipe();
    private Step mSelectedStep = new Step();

    private int mPosition = -1;
    private int mCurrentStepPosition = -1;

    @BindView(R.id.rv_step_details)
    RecyclerView rv_step_details;

    @BindView(R.id.btn_Previous)
    Button btn_Previous;

    @BindView(R.id.btn_Next)
    Button btn_Next;

    @BindView(R.id.playerView)
    SimpleExoPlayerView mPlayerView;

    private StepDetailsAdapter mStepsDetailsAdapter;

    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private long mCurrentPlayerPosition = C.TIME_UNSET;

    public StepDetails_Fragment(){}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();

        mSelectedRecipe = b.getParcelable("RecipeDetails");
        mPosition = b.getInt("Position");

        if (mSelectedRecipe != null && mPosition != -1) {
            Timber.d(mSelectedRecipe.toString());
        } else {
            Timber.d("Recipe is null");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("CurrentStepPosition",mCurrentStepPosition);

        // Get current player position & save it on the bundle

        outState.putLong("CurrentPlayPosition",mCurrentPlayerPosition);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.stepdetails_fragment,container,false);

        ButterKnife.bind(this, view);

        if( savedInstanceState==null ) {
            mCurrentStepPosition = mPosition - 1;
            mSelectedStep = mSelectedRecipe.getSteps().get(mPosition - 1);
        }
        else{
            mCurrentStepPosition = savedInstanceState.getInt("CurrentStepPosition");
            mSelectedStep = mSelectedRecipe.getSteps().get(mCurrentStepPosition);

            mCurrentPlayerPosition = savedInstanceState.getLong("CurrentPlayPosition", C.TIME_UNSET);
        }

        getActivity().setTitle(mSelectedRecipe.getName());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());

        // Set the layout manager
        rv_step_details.setLayoutManager(linearLayoutManager);

        // Make sure recyclerview size does not change when items are added or removed from it
        rv_step_details.setHasFixedSize(true);

        mStepsDetailsAdapter = new StepDetailsAdapter(mSelectedStep, getContext());

        mStepsDetailsAdapter.setSelectedStepeData(mSelectedStep, getContext());

        rv_step_details.setAdapter(mStepsDetailsAdapter);

        // Initialize the Media Session.
        initializeMediaSession();

        // Initialize the playser
        initializePlayer(Uri.parse(mSelectedStep.getVideoURL()));

        // Get the position of the step clicked
        // mCurrentStepPosition = mPosition - 1;
        UpdateNavigationButtons();

        btn_Previous.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Stop the current video from playing
                mExoPlayer.stop();

                mCurrentStepPosition--;
                mSelectedStep = mSelectedRecipe.getSteps().get(mCurrentStepPosition);
                mStepsDetailsAdapter.setSelectedStepeData(mSelectedStep, getContext());

                // Get the right video based on the step selected
                setPlayerUti(Uri.parse(mSelectedStep.getVideoURL()));

                UpdateNavigationButtons();
            }
        });

        btn_Next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                // Stop the current video from playing
                mExoPlayer.stop();

                mCurrentStepPosition++;
                mSelectedStep = mSelectedRecipe.getSteps().get(mCurrentStepPosition);
                mStepsDetailsAdapter.setSelectedStepeData(mSelectedStep, getContext());

                // Get the right video based on the step selected
                setPlayerUti(Uri.parse(mSelectedStep.getVideoURL()));

                UpdateNavigationButtons();
            }
        });

        return view;
    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getContext(), StepDetails_Fragment.class.getSimpleName());

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);
    }

    private void initializePlayer(Uri parse) {
        if (mExoPlayer == null) {

            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
                if( tabletSize==false){
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Timber.d("Phone in landscape");

                            getActivity().getWindow().getDecorView().setSystemUiVisibility(

                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE
                            );
                    }
                }
                else
                    Timber.d("Its a tablet");


            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            // Set the ExoPlayer.EventListener to this fragment
            mExoPlayer.addListener(this);

            // Prepare the MediaSource
            setPlayerUti(parse);
        }
    }

    private void setPlayerUti(Uri parse){

        if( parse.getPath().isEmpty() ){
            mPlayerView.setVisibility(View.GONE);
        }
        else {
            mPlayerView.setVisibility(View.VISIBLE);
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mMediaSource = new ExtractorMediaSource(parse, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            if (mCurrentPlayerPosition != C.TIME_UNSET)
                mExoPlayer.seekTo(mCurrentPlayerPosition);

            mExoPlayer.prepare(mMediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }


    private void UpdateNavigationButtons() {

        if( mCurrentStepPosition==0 ) {   // First step so no need to show the Previous button
            btn_Previous.setVisibility(View.INVISIBLE);
        }
        else {
            btn_Previous.setVisibility(View.VISIBLE);
        }

        if( mCurrentStepPosition==mSelectedRecipe.getSteps().size()-1 ) {   // Last step so no need to show the Next button
            btn_Next.setVisibility(View.INVISIBLE);
        }
        else {
            btn_Next.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMediaSession.setActive(true);

        // Initialize the playser
        initializePlayer(Uri.parse(mSelectedStep.getVideoURL()));
    }

    @Override
    public void onPause() {
        super.onPause();

        mCurrentPlayerPosition = mExoPlayer.getCurrentPosition();

        releasePlayer();

        mMediaSession.setActive(false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Release the playser
    private void releasePlayer() {

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }
        mMediaSession.setPlaybackState(mStateBuilder.build());
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            mExoPlayer.seekTo(0);
        }
    }

}
