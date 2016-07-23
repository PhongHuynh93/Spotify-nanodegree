package dhbk.android.spotifygcs.io.callback;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist.SearchResultsPresenter;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Main callback from spotify api server, communicate search results to {@link SearchResultsPresenter}
 */
public interface TopTrackSearchServerCallback {
    void onTopTracksFound(ArrayList<TopTrack> topTracks);
    void onFailedSearch();
}
