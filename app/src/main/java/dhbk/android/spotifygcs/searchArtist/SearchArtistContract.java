package dhbk.android.spotifygcs.searchArtist;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;

/**
 * Created by phongdth.ky on 7/14/2016.
 * contain view and presenter
 */
public interface SearchArtistContract {
    interface View extends BaseView<Presenter> {
    }

    // method presenter search artist must override
    interface Presenter extends BasePresenter {

    }
}
