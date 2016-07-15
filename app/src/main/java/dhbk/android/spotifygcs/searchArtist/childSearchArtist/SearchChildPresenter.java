package dhbk.android.spotifygcs.searchArtist.childSearchArtist;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/15/2016.
 */
public class SearchChildPresenter implements SearchChildContract.Presenter{

    private final SearchChildContract.View mSearchChildView;

    public SearchChildPresenter(@NonNull SearchChildContract.View searchChildView) {
        mSearchChildView = checkNotNull(searchChildView, "tasksView cannot be null!");
        mSearchChildView.setPresenter(this);
    }

    // start views, so we can do anything to load the content in this method
    @Override
    public void start() {

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
}
