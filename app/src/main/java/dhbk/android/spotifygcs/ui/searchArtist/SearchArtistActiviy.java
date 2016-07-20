package dhbk.android.spotifygcs.ui.searchArtist;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SearchArtistActiviy extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artists);
        ButterKnife.bind(this);

        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true); // set the left arrow in toolbar

        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        SearchArtistFragment searchArtistFragment = (SearchArtistFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_SEARCH_ARTISTS);
        if (searchArtistFragment == null) {
            // Create the fragment
            searchArtistFragment = SearchArtistFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), searchArtistFragment, R.id.contentFrame_searchartist);
        }

        // Create the presenter
        SearchArtistPresenter mSearchArtistPresenter = new SearchArtistPresenter(searchArtistFragment);
    }

    // use this method for an activity to change font of text
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // TODO: 7/18/2016 set up navigation drawer
    // Set up the navigation drawer.
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
//                            case R.id.list_navigation_menu_item:
//                                // Do nothing, we're already on that screen
//                                break;
//                            case R.id.statistics_navigation_menu_item:
//                                Intent intent =
//                                        new Intent(TasksActivity.this, StatisticsActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//                                        | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                startActivity(intent);
//                                break;
                        default:
                            break;
                    }
                    // Close the navigation drawer when an item is selected.
                    menuItem.setChecked(true);
                    mDrawerLayout.closeDrawers();
                    return true;
                });
    }

}
