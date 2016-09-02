package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.domain.SpotifyConstant;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

/**
 * simply control between views(fragment) and presenter
 * when user click a artist, this activity'll show up
 * this activity will tell the top tracks of a artist which user has clicked
 */
public class ShowTopTracksActivity extends BaseActivity {
    @BindView(R.id.recyclerview_list_track_nav)
    RecyclerView mRecyclerviewListTrackNav;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    private ShowTopTracksFragment mShowTopTrackView;

    public static Intent createStartIntent(Context context, String artistId, String artistName) {
        Intent starter = new Intent(context, ShowTopTracksActivity.class);
        starter.putExtra(SpotifyConstant.ID_ARTIST, artistId);
        starter.putExtra(SpotifyConstant.NAME_ARTIST, artistName);
        return starter;
    }

    // anim view before close
    @Override
    public void onBackPressed() {
        mShowTopTrackView.expandImageAndFinish();
    }

    // want to use custom font
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
        final String artistId = getIntent().getStringExtra(SpotifyConstant.ID_ARTIST);
        final String nameArtist = getIntent().getStringExtra(SpotifyConstant.NAME_ARTIST);

        // create view
        mShowTopTrackView =
                (ShowTopTracksFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_SHOW_TOP_TRACKS);
        if (mShowTopTrackView == null) {
            mShowTopTrackView = ShowTopTracksFragment.newInstance(artistId, nameArtist);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mShowTopTrackView, R.id.contentFrame);
        }

        // Create the presenter and set view
        ShowTopTracksPresenter showTopTracksPresenter = new ShowTopTracksPresenter(mShowTopTrackView);

        // Set up the navigation drawer.
        mDrawerLayout.setStatusBarBackground(R.color.grey_dark3);
//        setupDrawerContent(mNavView);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // : add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
