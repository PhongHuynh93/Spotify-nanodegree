package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.BaseView;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;

/**
 * Created by phongdth.ky on 7/15/2016.
 * contains require method for view and presenter in showTopTrack
 */
public interface ShowTopTracksContract {
    interface View extends BaseView<Presenter> {
        // get the ArtistSearchInteractor for connect to the internet
        ArtistSearchInteractor getArtistSearchInteractor();
    }

    interface Presenter extends BasePresenter {
    }
}
