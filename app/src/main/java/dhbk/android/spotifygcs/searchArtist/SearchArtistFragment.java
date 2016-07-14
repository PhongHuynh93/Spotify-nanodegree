package dhbk.android.spotifygcs.searchArtist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.HelpUtil;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment implements SearchArtistContract.View {

    @BindView(R.id.textview_empty_search_artist_help_info)
    TextView mTextviewEmptySearchArtistHelpInfo;

    public SearchArtistFragment() {
    }

    public static SearchArtistFragment newInstance() {
        return new SearchArtistFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_artist_emtpy, container, false);
        ButterKnife.bind(this, view);
        initView();
        return view;
    }

    // set presenter for this view
    @Override
    public void setPresenter(SearchArtistContract.Presenter presenter) {

    }

    private void initView() {
        mTextviewEmptySearchArtistHelpInfo.setText(HelpUtil.getSpannedText(getContext(), R.string.search_artist_emtpy_help_text));
    }

}
