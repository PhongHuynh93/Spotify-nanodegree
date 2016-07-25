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
        TopTrackSearchServerCallback {
    public static final String PLAY_ICON = "play";
    public static final String STOP_ICON = "stop";
    private ShowTopTracksContract.View mShowTopTrackView;
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
        if (mShowTopTrackView != null) {
            mShowTopTrackView.setupAdapter();
            mShowTopTrackView.setupRecyclerView();
        }
    }

    @Override
    public void getTopTenTracks() {
        mSpotifyInteractor.performTopTrackSearch(mShowTopTrackView.getIdArtist(), this);
    }


    @Override
    public void resetPlayer() {
        if (mShowTopTrackView != null) {
            mShowTopTrackView.resetPlayer();
        }
    }

    @Override
    public void stopDoBackgroundThread() {
        mShowTopTrackView = null;
    }

    @Override
    public void changeIconToPlay() {
        mShowTopTrackView.changeIcon(PLAY_ICON);
    }

    @Override
    public void changeIconToStop() {
        mShowTopTrackView.changeIcon(STOP_ICON);
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
