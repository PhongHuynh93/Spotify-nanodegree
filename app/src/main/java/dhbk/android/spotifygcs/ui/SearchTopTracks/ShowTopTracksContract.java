package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.util.ArrayList;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;

/**
 * Created by phongdth.ky on 7/15/2016.
 * contains require method for view and presenter in showTopTrack
 */
public interface ShowTopTracksContract {
    interface View extends BaseView<Presenter> {
        // get the SpotifyInteractor for connect to the internet
        SpotifyInteractor getSpotifyInteractor();

        // setup recyclerview
        void setupRecyclerView();

        // setup adatper to add to recyclerview
        void setupAdapter();

        // get id of artist
        String getIdArtist();

        // show a top of tracks in recyclerview
        void showTrackOnList(ArrayList<TopTrack> topTracks);

        // anim before close view
        void expandImageAndFinish();

        // start service
        void startSpotifyService(TopTrack topTrack, int position);

        // set the text to the time that player is playing
        void setTrackDuration();

        // stop service
        void destroySpotifyService();

        // reset the player, stop playing or pause, refresh duration
        void resetPlayer();

        // change icon of play button
        void changeIcon(String icon);

        void startServiceSpotify(Intent spotifyServiceIntent);

        void restartServiceSpotify(Intent spotifyServiceIntent);

        void resumeServiceSpotify(Intent spotifyServiceIntent);

        void goToPreviousView();

        void setViewObject();

        void addListener();

        // get an intent to service
        Intent getIntentForService();
        Intent getIntentForService(String trackUrl);

        // method for control the play and pause state
        void setPlayState();

        int getPreviousTrackPosition();

        void startToPlayTrack(int position);

        void startToPlayTrack();

        void setPauseState();

        void startToPauseTrack();

        int getNextTrackPosition();
    }

    interface Presenter extends BasePresenter {
        // setup recyclerview and setup adapter
        void setupList();

        // connect to spotify to download top ten track
        void getTopTenTracks();

        // reset the field of player
        void resetPlayer();

        // stop duing background thread that update the view.
        void stopDoBackgroundThread();

        // change icon of playbutton to playbutton
        void changeIconToPlay();

        // change icon of playbutton to stopbutton
        void changeIconToStop();

        void startServiceSpotify(Intent spotifyServiceIntent);

        void restartServiceSpotify(Intent spotifyServiceIntent);

        void resumeServiceSpotify(Intent spotifyServiceIntent);

        // remove this view and go to view beneath
        void goBackToPreviousView();

        // init views object in views
        void initView();

        void playPreviousTrack();

        void playTrack();

        void pauseTrack();

        void playNextTrack();
    }

    // for spotify play music service
    interface MusicService {
        // set the URL for a music track to field
        void setTrackUrlPreview(String trackUrlPreview);

        // start to play a track
        void playTrack(int trackPosition);

        // stop play track, return time which the track is currently playing on
        int pauseTrack();

        //    init the player service and start to play music background
        void initSpotifyPlayer();

        // set handler for service
        void setSpotifyPlayerHandler(Handler spotifyPlayerHandler);

        // get the current position of the music that is playing
        Bundle getCurrentTrackPosition();

        // update views with the current location of tracks
        // update UI in views after 1s
        void updateUI();

//    terminate timer
        void noUpdateUI();

        // get the duration at the moment of a track
        int getTrackDuration();

        // change time in milisecond to text
        String getTrackDurationString();

    }
}
