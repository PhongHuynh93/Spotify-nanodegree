package dhbk.android.spotifygcs.searchArtist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.searchArtist.childSearchArtist.SearchChildActivity;
import dhbk.android.spotifygcs.util.HelpUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends Fragment implements SearchArtistContract.View {
    private static final int RC_SEARCH = 0;

    @BindView(R.id.textview_empty_search_artist_help_info)
    TextView mTextviewEmptySearchArtistHelpInfo;
    private SearchArtistContract.Presenter mPresenter;

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
        setHasOptionsMenu(true);
        return view;
    }

    // start to run the presenter, example such as load the db
    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }

    //    menu toolbar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_artist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                mPresenter.loadSearchSetting();
                break;
            case R.id.menu_about:
                mPresenter.loadAboutSetting();
                break;
        }
        return true;
    }


    // set presenter for this view
    @Override
    public void setPresenter(SearchArtistContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    // call when user click the search icon, the view'll navigate to another activity to search
    @Override
    public void startSearchActivity() {
        // get the icon's location on screen to pass through to the search screen
        View searchMenuView = getActivity().findViewById(R.id.menu_search);
        int[] loc = new int[2];
        searchMenuView.getLocationOnScreen(loc);

        startActivityForResult(SearchChildActivity.createStartIntent(getContext(),
                loc[0], loc[0] + (searchMenuView.getWidth() / 2)), RC_SEARCH,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        searchMenuView.setAlpha(0f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SEARCH:
                // reset the search icon which we hid
                View searchMenuView = getActivity().findViewById(R.id.menu_search);
                if (searchMenuView != null) {
                    searchMenuView.setAlpha(1f);
                }
        }
    }

    private void initView() {
        mTextviewEmptySearchArtistHelpInfo.setText(HelpUtil.getSpannedText(getContext(), R.string.search_artist_emtpy_help_text));
    }

}
