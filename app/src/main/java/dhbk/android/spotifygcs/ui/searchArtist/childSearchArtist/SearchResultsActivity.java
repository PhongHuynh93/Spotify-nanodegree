package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;

import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

public class SearchResultsActivity extends BaseActivity {
    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
    private SearchResultsPresenter mSearchResultsPresenter;
    private SearchResultsFragment mView;

    // intent to go to searchChildActivity, with anim search icon on toolbar, so get the location of it
    public static Intent createStartIntent(Context context, int menuIconLeft, int menuIconCenterX) {
        Intent starter = new Intent(context, SearchResultsActivity.class);
        starter.putExtra(EXTRA_MENU_LEFT, menuIconLeft);
        starter.putExtra(EXTRA_MENU_CENTER_X, menuIconCenterX);
        return starter;
    }

    @Override
    public void onBackPressed() {
        mSearchResultsPresenter.dismissView();
    }

    @Override
    protected boolean hasUseCustomeFont() {
        return true;
    }

    @Override
    public int getLayout() {
        return R.layout.activity_content_frame;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        // extract the search icon's location passed from the launching activity, minus 4dp to
        // compensate for different paddings in the views
        final int searchBackDistanceX = getIntent().getIntExtra(EXTRA_MENU_LEFT, 0) - (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        final int searchIconCenterX = getIntent().getIntExtra(EXTRA_MENU_CENTER_X, 0);

        mView =
                (SearchResultsFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_TEST_CHILD_SEARCH_ARTISTS);
        if (mView == null) {
            // Create the fragment
            mView = SearchResultsFragment.newInstance(searchBackDistanceX, searchIconCenterX);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mView, R.id.contentFrame);
        }

        // Create the presenter
        mSearchResultsPresenter = new SearchResultsPresenter(mView);

        // You can invoke onNewIntent always by putting it into onCreate method like
        onNewIntent(getIntent());
    }

    // because this activity is single top
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(SearchManager.QUERY)) {
            mView.setQueryToSearchBar(intent.getStringExtra(SearchManager.QUERY));
        }
    }

}
