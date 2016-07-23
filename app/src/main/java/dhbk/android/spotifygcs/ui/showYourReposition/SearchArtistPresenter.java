package dhbk.android.spotifygcs.ui.showYourReposition;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/14/2016.
 * presenter for search artist fragment
 */
public class SearchArtistPresenter implements SearchArtistContract.Presenter{

    private final SearchArtistContract.View mSearchArtistView;

    public SearchArtistPresenter(@NonNull SearchArtistContract.View searchArtistView) {
        mSearchArtistView = checkNotNull(searchArtistView, "tasksView cannot be null!");
        mSearchArtistView.setPresenter(this);
    }

    // start the presenter such as load the db, but in my app, the presenter do nothing when view is on the top of screen.
    @Override
    public void start() {
    }

    // call when user click the search icon in toolbar
    @Override
    public void loadSearchSetting() {
        mSearchArtistView.startSearchActivity();
    }

    // call when user click the about setting icon in toolbar
    @Override
    public void loadAboutSetting() {

    }
}
