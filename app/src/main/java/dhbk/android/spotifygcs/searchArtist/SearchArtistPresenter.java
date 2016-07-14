package dhbk.android.spotifygcs.searchArtist;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/14/2016.
 */
public class SearchArtistPresenter implements SearchArtistContract.Presenter{

    private final SearchArtistContract.View mSearchArtistView;

    public SearchArtistPresenter(@NonNull SearchArtistContract.View searchArtistView) {
        mSearchArtistView = checkNotNull(searchArtistView, "tasksView cannot be null!");
        mSearchArtistView.setPresenter(this);
    }

    // start the presenter
    @Override
    public void start() {
    }

}
