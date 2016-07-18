package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import java.util.ArrayList;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;

/**
 * Created by phongdth.ky on 7/15/2016.
 */
public interface SearchChildContract {
    // method for views  - fragments
    interface View extends BaseView<Presenter> {
        // open this view and anim the search bar
        void animSearchView();
        // close this view
        void dismiss();
        // declare whether view is add or not.
        boolean isActive();
        // setup recyclerview
        void setupRecyclerView();
        // setup adatper to add to recyclerview
        void setupAdapter();
        // setup searchbar
        void setupSearchBar();
        // get the ArtistSearchInteractor for connect to the internet
        ArtistSearchInteractor getArtistSearchInteractor();
        // display artists in recyclerview
        void displaySearchArtists(ArrayList<Artist> artists);
        // remove the progress bar
        // set the rcv to visible
        void showtoRcv();
        // if we dont have data to show, info the user
        void showEmptyArtistsLayout();
    }

    // method presenter search artist must override
    interface Presenter extends BasePresenter {
        // remove the view of this presenter
        void dismissView();

        // anim search bar of views
        void setupSearchBar();

        // setup recyclerview and setup adapter
        void setupList();

        // search artist with string para
        void searchArtists(String query);

        // do other things to views before showing the data to views.
        void doOtherThingToShowResults();

        // if we dont have data to show, info the user
        void infoUsersNotHaveData();
    }
}
