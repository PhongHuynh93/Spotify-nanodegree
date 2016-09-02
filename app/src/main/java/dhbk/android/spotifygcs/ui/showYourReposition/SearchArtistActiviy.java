package dhbk.android.spotifygcs.ui.showYourReposition;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

import static com.google.common.base.Preconditions.checkNotNull;

public class SearchArtistActiviy extends BaseActivity {
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    // todo other activity will use this method to go to this activity
    public static Intent createStartIntent(Context context) {
        return new Intent(context, SearchArtistActiviy.class);
    }

    @Override
    public int getLayout() {
        return R.layout.activity_search_artists;
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        getSupportActionBar().setTitle(R.string.app_name);
        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        setupDrawerContent(mNavView);

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

    @Override
    protected boolean hasUseCustomeFont() {
        return true;
    }

    private void setupDrawerContent(NavigationView navigationView) {
        checkNotNull(navigationView, "Cann't find navigationview. Did you inflate this yet");
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    switch (menuItem.getItemId()) {
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
