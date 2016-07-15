package dhbk.android.spotifygcs.io.callback;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.searchArtist.childSearchArtist.SearchChildPresenter;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Main callback from spotify api server, communicate search results to {@link SearchChildPresenter}
 */
public interface ArtistSearchServerCallback {
    // when we found the artists from query
    void onArtistsFound(ArrayList<Artist> artists);
    // when we not found the artists
    void onFailedSearch();
}
