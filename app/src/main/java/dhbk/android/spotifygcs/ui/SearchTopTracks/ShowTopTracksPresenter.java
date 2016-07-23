package dhbk.android.spotifygcs.ui.SearchTopTracks;

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
        TopTrackSearchServerCallback{
    private final ShowTopTracksContract.View mShowTopTrackView;
    private SpotifyInteractor mSpotifyInteractor;

    public ShowTopTracksPresenter(@NonNull ShowTopTracksContract.View showTopTrackView) {
        mShowTopTrackView = checkNotNull(showTopTrackView, "tasksView cannot be null!");
        mShowTopTrackView.setPresenter(this);
    }


    // works when views start
    @Override
    public void start() {
        // get the SpotifyInteractor for connect to the internet
        mSpotifyInteractor = mShowTopTrackView.getSpotifyInteractor();
        // setup recyclerview and setup adapter
        setupList();
        // connect to spotify to download top ten track
        getTopTenTracks();
    }

    @Override
    public void setupList() {
        mShowTopTrackView.setupAdapter();
        mShowTopTrackView.setupRecyclerView();
    }

    @Override
    public void getTopTenTracks() {
        mSpotifyInteractor.performTopTrackSearch(mShowTopTrackView.getIdArtist(), this);
    }

    // when top track found
    @Override
    public void onTopTracksFound(ArrayList<TopTrack> topTracks) {
        mShowTopTrackView.showTrackOnList(topTracks);
    }

    // when not found
    @Override
    public void onFailedSearch() {

    }
}
