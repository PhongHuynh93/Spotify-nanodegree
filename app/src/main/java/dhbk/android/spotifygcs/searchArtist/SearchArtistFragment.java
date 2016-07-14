package dhbk.android.spotifygcs.searchArtist;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dhbk.android.spotifygcs.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment implements SearchArtistContract.View{

    public SearchArtistFragment() {
    }

    public static SearchArtistFragment newInstance() {
        return new SearchArtistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_artist_emtpy, container, false);
    }

    // set presenter for this view
    @Override
    public void setPresenter(SearchArtistContract.Presenter presenter) {

    }
}
