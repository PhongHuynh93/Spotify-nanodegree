package dhbk.android.spotifygcs.io.callback;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.ui.SearchTopTracks.ShowTopTracksPresenter;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Main callback from spotify api server, communicate search results to {@link ShowTopTracksPresenter}
 */
public interface TopTrackSearchServerCallback {
    // callback when top tracks of a artist was found
    void onTopTracksFound(ArrayList<TopTrack> topTracks);

    // callback when search failed
    void onFailedSearch();
}
