package dhbk.android.spotifygcs.ui.SearchArtist;

import android.app.ActivityOptions;
import android.app.SearchManager;
import android.app.SharedElementCallback;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;

import java.util.List;
import java.util.Map;

import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.ui.SearchTopTracks.ShowTopTracksActivity;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

/**
 * simply control between views(fragment) and presenter
 * search for artists that the user has searched.
 */
public class SearchResultsActivity extends BaseActivity {
    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
    private static final int REQUEST_CODE_VIEW_SHOT = 5407;

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

        mView = (SearchResultsFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_TEST_CHILD_SEARCH_ARTISTS);
        if (mView == null) {
            mView = SearchResultsFragment.newInstance(searchBackDistanceX, searchIconCenterX);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mView, R.id.contentFrame);
        }

        // Create the presenter
        mSearchResultsPresenter = new SearchResultsPresenter(mView);

        // You can invoke onNewIntent always by putting it into onCreate method like
        onNewIntent(getIntent());

        setExitSharedElementCallback(createSharedElementReenterCallback());
    }

    // because this activity is single top
    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.hasExtra(SearchManager.QUERY)) {
            mView.setQueryToSearchBar(intent.getStringExtra(SearchManager.QUERY));
        }
    }

    // image is a shared element between two activity
    public void goToAnotherActivity(View image, String idArtist, String nameArtist) {
        // anim when open second activity, with 2 share element
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this,
                Pair.create(image, getString(R.string.transition_shot)),
                Pair.create(image, getString(R.string.transition_shot_background)));

        // pass id of a artist to second activity
        startActivityForResult(ShowTopTracksActivity.createStartIntent(this, idArtist, nameArtist), REQUEST_CODE_VIEW_SHOT, options.toBundle());
    }


    private SharedElementCallback createSharedElementReenterCallback() {
        final String shotTransitionName = getString(R.string.transition_shot);
        final String shotBackgroundTransitionName = getString(R.string.transition_shot_background);
        return new SharedElementCallback() {
            /**
             * We're performing a slightly unusual shared element transition i.e. from one view
             * (image in the grid) to two views (the image & also the background of the details
             * view, to produce the expand effect). After changing orientation, the transition
             * system seems unable to map both shared elements (only seems to map the shot, not
             * the background) so in this situation we manually map the background to the
             * same view.
             */
            @Override
            public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
                if (sharedElements.size() != names.size()) {
                    // couldn't map all shared elements
                    final View sharedShot = sharedElements.get(shotTransitionName);
                    if (sharedShot != null) {
                        // has shot so add shot background, mapped to same view
                        sharedElements.put(shotBackgroundTransitionName, sharedShot);
                    }
                }
            }
        };
    }
}
