package dhbk.android.spotifygcs.searchArtist.childSearchArtist;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;

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
        // declare whether view is live or not.
        boolean isActive();
        // setup recyclerview
        void setupRecyclerView();
        // setup adatper to add to recyclerview
        void setupAdapter();
    }

    // method presenter search artist must override
    interface Presenter extends BasePresenter {
        // remove the view of this presenter
        void dismissView();

        // anim search bar of views
        void animTheSearchBar();

        // setup recyclerview and setup adapter
        void setupList();
    }
}
