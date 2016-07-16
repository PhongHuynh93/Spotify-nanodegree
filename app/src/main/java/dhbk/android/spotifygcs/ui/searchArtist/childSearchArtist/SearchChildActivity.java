package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;

import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

public class SearchChildActivity extends AppCompatActivity {
    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
    private SearchChildPresenter mSearchChildPresenter;

    public static Intent createStartIntent(Context context, int menuIconLeft, int menuIconCenterX) {
        Intent starter = new Intent(context, SearchChildActivity.class);
        starter.putExtra(EXTRA_MENU_LEFT, menuIconLeft);
        starter.putExtra(EXTRA_MENU_CENTER_X, menuIconCenterX);
        return starter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_child);
        ButterKnife.bind(this);

        // extract the search icon's location passed from the launching activity, minus 4dp to
        // compensate for different paddings in the views
        final int searchBackDistanceX = getIntent().getIntExtra(EXTRA_MENU_LEFT, 0) - (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        final int searchIconCenterX = getIntent().getIntExtra(EXTRA_MENU_CENTER_X, 0);

        SearchChildFragment searchChildFragment =
                (SearchChildFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_CHILD_SEARCH_ARTISTS);
        if (searchChildFragment == null) {
            // Create the fragment
            searchChildFragment = SearchChildFragment.newInstance(searchBackDistanceX, searchIconCenterX);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), searchChildFragment, R.id.contentFrame);
        }

        // Create the presenter
        mSearchChildPresenter = new SearchChildPresenter(searchChildFragment);
    }

    // when press back press, call view to anim and finish activity.
    @Override
    public void onBackPressed() {
        mSearchChildPresenter.dismissView();
//        super.onBackPressed(); // default action is finish the activity
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}