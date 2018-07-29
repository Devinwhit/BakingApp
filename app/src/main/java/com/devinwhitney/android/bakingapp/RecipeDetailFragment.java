package com.devinwhitney.android.bakingapp;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devinwhitney.android.bakingapp.model.Steps;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by devin on 7/28/2018.
 */

public class RecipeDetailFragment extends Fragment {
    public static String STEPS = "steps_extra";

    private Steps mStep;
    private TextView mStepDescription;

    private String fragment;
    private static final String RecipeActivityClassName = "RecipeDetailActivity";

    private Button mNextButton;
    private Button mPreviousButton;



    OnStepClickListener mCallback;

    public interface OnStepClickListener {
        void onButtonSelection(String buttonDirection, int stepNum);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fragment = getActivity().getLocalClassName();
        if (fragment.equals(RecipeActivityClassName)) {
            mCallback = (OnStepClickListener) context;
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mStep = savedInstanceState.getParcelable(STEPS);
        } else {
            if (getArguments() != null){
                mStep = getArguments().getParcelable(STEPS);
            } else {
                mStep = new Steps();
            }

        }

        View view = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        mStepDescription = view.findViewById(R.id.recipe_step_instruction);
        mStepDescription.setText(mStep.getDescription());
        mNextButton = view.findViewById(R.id.next_step_button);
        mPreviousButton = view.findViewById(R.id.previous_step_button);
        if (fragment.equals(RecipeActivityClassName)){
            mNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onButtonSelection("next", Integer.parseInt(mStep.getId()));
                }
            });

            mPreviousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallback.onButtonSelection("previous", Integer.parseInt(mStep.getId()));
                }
            });
        } else {
            mNextButton.setVisibility(View.INVISIBLE);
            mPreviousButton.setVisibility(View.INVISIBLE);
        }
        return view;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Steps steps = mStep;
        outState.putParcelable(STEPS, steps);
    }


}
