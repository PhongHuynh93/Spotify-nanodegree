package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Intent;
import android.support.annotation.NonNull;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.io.callback.TopTrackSearchServerCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/20/2016.
 */
public class ShowTopTracksPresenter implements ShowTopTracksContract.Presenter,
        TopTrackSearchServerCallback {
    public static final String PLAY_ICON = "play";
    public static final String STOP_ICON = "stop";
    private ShowTopTracksContract.View mView;
    private SpotifyInteractor mSpotifyInteractor;

    public ShowTopTracksPresenter(@NonNull ShowTopTracksContract.View showTopTrackView) {
        mView = checkNotNull(showTopTrackView, "tasksView cannot be null!");
        mView.setPresenter(this);
    }

    // works when views start
    @Override
    public void start() {
        // get the SpotifyInteractor for connect to the internet
        mSpotifyInteractor = mView.getSpotifyInteractor();
        // setup recyclerview and setup adapter
        setupList();
        // connect to spotify to download top ten track
        getTopTenTracks();
    }

    // init views object
    // init listener for views
    @Override
    public void initView() {
        mView.setViewObject();
        mView.addListener();

    }

    // setup recyclerview views
    @Override
    public void setupList() {
        mView.setupAdapter();
        mView.setupRecyclerView();
    }

    @Override
    public void getTopTenTracks() {
        mSpotifyInteractor.performTopTrackSearch(mView.getIdArtist(), this);
    }


    @Override
    public void resetPlayer() {
        mView.resetPlayer();
    }

    @Override
    public void stopDoBackgroundThread() {
        mView = null;
    }

    @Override
    public void changeIconToPlay() {
        mView.changeIcon(PLAY_ICON);
    }

    @Override
    public void changeIconToStop() {
        mView.changeIcon(STOP_ICON);
    }

    // when top track found
    @Override
    public void onTopTracksFound(ArrayList<TopTrack> topTracks) {
        mView.showTrackOnList(topTracks);
    }

    // when not found
    @Override
    public void onFailedSearch() {

    }

    // start service to play music
    @Override
    public void startServiceSpotify(Intent spotifyServiceIntent) {
        if (mView != null) {
            mView.startServiceSpotify(spotifyServiceIntent);
        }
    }

    // nav to new track, so stop old service and start new one
    @Override
    public void restartServiceSpotify(Intent spotifyServiceIntent) {
        if (mView != null) {
            mView.restartServiceSpotify(spotifyServiceIntent);
        }
    }

    // nav to old service, so not stop the service
    @Override
    public void resumeServiceSpotify(Intent spotifyServiceIntent) {
        mView.resumeServiceSpotify(spotifyServiceIntent);
    }

    @Override
    public void goBackToPreviousView() {
        mView.goToPreviousView();
    }

    @Override
    public void playPreviousTrack() {
        mView.setPlayState();
        int position = mView.getPreviousTrackPosition();
        mView.startToPlayTrack(position);
    }

    @Override
    public void playTrack() {
        mView.setPlayState();
        mView.startToPlayTrack();
    }

    @Override
    public void pauseTrack() {
        mView.setPauseState();
        mView.startToPauseTrack();
    }

    @Override
    public void playNextTrack() {
        mView.setPlayState();
        int position = mView.getNextTrackPosition();
        mView.startToPlayTrack(position);
    }
}
