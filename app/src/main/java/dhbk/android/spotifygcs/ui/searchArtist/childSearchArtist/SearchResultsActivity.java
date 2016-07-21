package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

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
    private SearchChildPresenter mSearchChildPresenter;

    // intent to go to searchChildActivity, with anim search icon on toolbar, so get the location of it
    public static Intent createStartIntent(Context context, int menuIconLeft, int menuIconCenterX) {
        Intent starter = new Intent(context, SearchResultsActivity.class);
        starter.putExtra(EXTRA_MENU_LEFT, menuIconLeft);
        starter.putExtra(EXTRA_MENU_CENTER_X, menuIconCenterX);
        return starter;
    }

    // when press back press, call view to anim and finish activity.
//    @Override
//    public void onBackPressed() {
//        mSearchChildPresenter.dismissView();
////        super.onBackPressed(); // default action is finish the activity
//    }

    @Override
    protected void doWhenPressBackButton() {
        mSearchChildPresenter.dismissView();
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

        SearchResultsFragment searchChildFragment =
                (SearchResultsFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_TEST_CHILD_SEARCH_ARTISTS);
        if (searchChildFragment == null) {
            // Create the fragment
            searchChildFragment = SearchResultsFragment.newInstance(searchBackDistanceX, searchIconCenterX);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), searchChildFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSearchChildPresenter = new SearchChildPresenter(searchChildFragment);
    }

}
