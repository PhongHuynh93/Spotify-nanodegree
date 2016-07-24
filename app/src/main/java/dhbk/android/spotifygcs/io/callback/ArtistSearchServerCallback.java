package dhbk.android.spotifygcs.io.callback;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.ui.searchArtist.SearchResultsPresenter;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Main callback from spotify api server, communicate search results to {@link SearchResultsPresenter}
 */
public interface ArtistSearchServerCallback {
    // when we found the artist from query
    void onArtistsFound(ArrayList<Artist> artists);
    // when we not found the artist
    void onFailedSearch();
}
