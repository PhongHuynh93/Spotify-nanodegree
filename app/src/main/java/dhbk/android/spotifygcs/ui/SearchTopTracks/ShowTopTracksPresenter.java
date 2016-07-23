package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.io.callback.TopTrackSearchServerCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/20/2016.
 */
public class ShowTopTracksPresenter implements ShowTopTracksContract.Presenter,
        TopTrackSearchServerCallback{
    private final ShowTopTracksContract.View mShowTopTrackView;
    private ArtistSearchInteractor mArtistSearchInteractor;

    public ShowTopTracksPresenter(@NonNull ShowTopTracksContract.View showTopTrackView) {
        mShowTopTrackView = checkNotNull(showTopTrackView, "tasksView cannot be null!");
        mShowTopTrackView.setPresenter(this);
    }


    // works when views start
    @Override
    public void start() {
        // get the ArtistSearchInteractor for connect to the internet
        mArtistSearchInteractor = mShowTopTrackView.getArtistSearchInteractor();
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
        mArtistSearchInteractor.performTopTrackSearch(mShowTopTrackView.getIdArtist(), this);
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
