package dhbk.android.spotifygcs.ui.showYourReposition;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.ui.searchArtist.SearchResultsActivity;
import dhbk.android.spotifygcs.util.HelpUtil;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A placeholder fragment containing a simple view.
 */
public class SearchArtistFragment extends BaseFragment implements SearchArtistContract.View {
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
    public int getLayout() {
        return R.layout.fragment_search_artist_emtpy;
    }

    @Override
    protected void doThingWhenResumeApp() {

    }

    @Override
    protected void doThingWhenPauseApp() {

    }

    @Override
    protected void doThingWhenDestroyApp() {
        mPresenter = null;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        mTextviewEmptySearchArtistHelpInfo.setText(HelpUtil.getSpannedText(getContext(), R.string.search_artist_emtpy_help_text));
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(getActivity());
                break;
            default:
                break;
        }
        return true;
    }

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

        // not define  transition
        startActivityForResult(
                SearchResultsActivity.createStartIntent(getContext(), loc[0], loc[0] + (searchMenuView.getWidth() / 2)),
                RC_SEARCH,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
        searchMenuView.setAlpha(0f);
    }

    // TODO: 7/18/2016 implement this method, if we find an artist, what to do next
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RC_SEARCH:
                // Make sure the request was successful
                // reset the search icon which we hid
                View searchMenuView = getActivity().findViewById(R.id.menu_search);
                if (searchMenuView != null) {
                    searchMenuView.setAlpha(1f);
                }
                break;
            default:
                break;
        }
    }
}
