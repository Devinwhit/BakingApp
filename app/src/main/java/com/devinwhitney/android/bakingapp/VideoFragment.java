package com.devinwhitney.android.bakingapp;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
import com.squareup.picasso.Picasso;

/**
 * Created by devin on 7/28/2018.
 */

public class VideoFragment extends Fragment {

    private static final String POSITION = "position_state";
    private static final String TAG = VideoFragment.class.getName();
    private static final String PLAYBACK = "playback";
    private SimpleExoPlayer mExoPlayer;
    private ImageView mImageView;
    private SimpleExoPlayerView mPlayerView;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;

    private Long mPosition;
    private boolean mPlayback;

    private static final String VIDEO_URL = "video";
    public static final String VIDEO_THUMBNAIL_URL = "video_thumbnail";
    private String videoThumbnail;
    private String videoURL;
    public VideoFragment() {
        //empty constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mPosition = 0L;
        if (savedInstanceState != null) {
            videoURL = savedInstanceState.getString(VIDEO_URL);
            mPosition = savedInstanceState.getLong(POSITION, C.TIME_UNSET);
            videoThumbnail = savedInstanceState.getString(VIDEO_THUMBNAIL_URL);
            mPlayback = savedInstanceState.getBoolean(PLAYBACK);
        } else {
            if (getArguments() != null) {
                videoURL = getArguments().getString(VIDEO_URL);
            } else {
                videoURL = "";
            }

        }

        View view = inflater.inflate(R.layout.just_video, container, false);
        mImageView = view.findViewById(R.id.video_image);
        mPlayerView = view.findViewById(R.id.playerView);
        if (!videoURL.equals("")) {
            if (mImageView.getVisibility() == View.VISIBLE) mImageView.setVisibility(View.INVISIBLE);

            initializeMediaSession();
            initializePlayer(Uri.parse(videoURL));

        } else {
            mImageView.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mPlayerView.getLayoutParams();
            layoutParams.width = 0;
            layoutParams.height = 0;

            mPlayerView.setLayoutParams(layoutParams);
             Picasso.get().load(videoThumbnail).into(mImageView);
        }
        return view;

    }

    private void initializeMediaSession() {

        mMediaSession = new MediaSessionCompat(getContext(), TAG);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        mMediaSession.setMediaButtonReceiver(null);

        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());

        mMediaSession.setCallback(new VideoFragment.MySessionCallback());

        mMediaSession.setActive(true);
    }
    /**
     * Initialize ExoPlayer.
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.seekTo(mPosition);

            mExoPlayer.addListener(new ExoPlayer.EventListener() {
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

                    mPlayback = playWhenReady;
                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity() {

                }
            });

            // Set the ExoPlayer.EventListener to this activity.
            //mExoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), "BakingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            mExoPlayer.prepare(mediaSource);
            if (!mPlayback) {
                mExoPlayer.setPlayWhenReady(false);
            } else {
                mExoPlayer.setPlayWhenReady(true);
            }


        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mExoPlayer != null) {
            long position = mExoPlayer.getCurrentPosition();
            outState.putLong(POSITION, position);
            outState.putString(VIDEO_URL, videoURL);
            outState.putString(VIDEO_THUMBNAIL_URL, videoThumbnail);
            outState.putBoolean(PLAYBACK, mPlayback);
        }


    }

    private void releasePlayer() {
        if (mExoPlayer!= null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }




    @Override
    public void onStop() {
        super.onStop();
        if (mExoPlayer != null) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }
/*
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) mPlayerView.getLayoutParams();
            layoutParams.width = layoutParams.MATCH_PARENT;
            layoutParams.height = layoutParams.MATCH_PARENT;

            mPlayerView.setLayoutParams(layoutParams);
        }

    }
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
    }
}
