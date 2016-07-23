package dhbk.android.spotifygcs.ui.SearchArtist;

import android.support.annotation.NonNull;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.io.callback.ArtistSearchServerCallback;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/15/2016.
 */
public class SearchResultsPresenter implements SearchResultsContract.Presenter, ArtistSearchServerCallback {

    private final SearchResultsContract.View mSearchChildView;
    private ArtistSearchInteractor mArtistSearchInteractor;

    public SearchResultsPresenter(@NonNull SearchResultsContract.View searchChildView) {
        mSearchChildView = checkNotNull(searchChildView, "tasksView cannot be null!");
        mSearchChildView.setPresenter(this);
    }

    // start views, so we can do anything to load the content in this method
    @Override
    public void start() {
        // get the ArtistSearchInteractor for connect to the internet
        mArtistSearchInteractor = mSearchChildView.getArtistSearchInteractor();
        // start search bar anim in view and setup searchbar
        setupSearchBar();
        // setup recyclerview and setup adapter
        setupList();
    }

    // close views
    @Override
    public void dismissView() {
        mSearchChildView.dismiss();
    }

    // animation the searchbar and setup it.
    @Override
    public void setupSearchBar() {
        mSearchChildView.animSearchView();
        mSearchChildView.setupSearchBar();
    }

    // setup recyclerview and adapter of a list
    @Override
    public void setupList() {
        mSearchChildView.setupAdapter();
        mSearchChildView.setupRecyclerView();
    }

    // search artist with string para
    @Override
    public void searchArtists(String query) {
        mArtistSearchInteractor.performSearch(query, this);
    }

    // if we dont have data to show, info the user
    @Override
    public void infoUsersNotHaveData() {
        mSearchChildView.showEmptyArtistsLayout();
    }

    // callback when query the spotify api, if found the artists
    @Override
    public void onArtistsFound(ArrayList<Artist> artists) {
        mSearchChildView.displaySearchArtists(artists);
    }

    // callback when query the spotify api, if not found the artists
    @Override
    public void onFailedSearch() {
        infoUsersNotHaveData();
    }
}
