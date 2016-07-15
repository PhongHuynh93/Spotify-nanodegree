package dhbk.android.spotifygcs.searchArtist;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;

/**
 * Created by phongdth.ky on 7/14/2016.
 * contain view and presenter
 */
public interface SearchArtistContract {
    // method for views  - fragments
    interface View extends BaseView<Presenter> {
        // call when user click the search icon, the view'll navigate to another activity to search
        void startSearchActivity();
    }

    // method presenter search artist must override
    interface Presenter extends BasePresenter {
        // call when user click the search icon in toolbar
        void loadSearchSetting();
        // call when user click the about setting icon in toolbar
        void loadAboutSetting();
    }
}
