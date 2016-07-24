package dhbk.android.spotifygcs.ui.SearchTopTracks;

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
        void startSpotifyService(TopTrack topTrack);

        // set the text to the time that player is playing
        void setTrackDuration();

    }

    interface Presenter extends BasePresenter {
        // setup recyclerview and setup adapter
        void setupList();

        // connect to spotify to download top ten track
        void getTopTenTracks();
    }

    // for spotify play music service
    interface MusicService {
        // set the URL for a music track to field
        void setTrackUrlPreview(String trackUrlPreview);

        // start to play a track
        void playTrack(int trackPosition);

        // set listener when play success or fail
        void initSpotifyPlayer();

        // set handler for service
        void setSpotifyPlayerHandler(Handler spotifyPlayerHandler);

        // get the current position of the music that is playing
        Bundle getCurrentTrackPosition();

        void updateUI();

        void noUpdateUI();

        // get the duration at the moment of a track
        int getTrackDuration();

        // change time in milisecond to text
        String getTrackDurationString();

    }
}
