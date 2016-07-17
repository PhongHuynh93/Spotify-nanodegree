package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.io.callback.ArtistSearchServerCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/15/2016.
 */
public class SearchChildPresenter implements SearchChildContract.Presenter, ArtistSearchServerCallback {

    private final SearchChildContract.View mSearchChildView;
    private ArtistSearchInteractor mArtistSearchInteractor;

    public SearchChildPresenter(@NonNull SearchChildContract.View searchChildView) {
        mSearchChildView = checkNotNull(searchChildView, "tasksView cannot be null!");
        mSearchChildView.setPresenter(this);
    }

    // start views, so we can do anything to load the content in this method
    @Override
    public void start() {
        // get the ArtistSearchInteractor for connect to the internet
        mArtistSearchInteractor = mSearchChildView.getArtistSearchInteractor();
        // start search bar anim in view
        animTheSearchBar();
        // setup recyclerview and setup adapter
        setupList();
    }

    // close views
    @Override
    public void dismissView() {
        mSearchChildView.dismiss();
    }

    // animation the searchbar
    @Override
    public void animTheSearchBar() {
        mSearchChildView.animSearchView();
    }

    // setup recyclerview and adapter of a list
    @Override
    public void setupList() {
        mSearchChildView.setupRecyclerView();
        mSearchChildView.setupAdapter();
    }

    // search artist with string para
    @Override
    public void searchArtists(String query) {
        mArtistSearchInteractor.performSearch(query, this);
    }

    // callback when query the spotify api, if found the artists
    @Override
    public void onArtistsFound(ArrayList<Artist> artists) {
        mSearchChildView.displaySearchArtists(artists);
    }

    // callback when query the spotify api, if not found the artists
    @Override
    public void onFailedSearch() {
        // TODO: 7/16/16 look at plaid to see if not found artist
    }
}
