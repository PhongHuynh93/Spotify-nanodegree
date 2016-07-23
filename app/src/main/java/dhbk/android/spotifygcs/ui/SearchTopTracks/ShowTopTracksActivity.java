package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Context;
import android.content.Intent;

import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

public class ShowTopTracksActivity extends BaseActivity {
    private ShowTopTracksPresenter mShowTopTracksPresenter;
    private ShowTopTracksFragment mShowTopTrackView;

    public static Intent createStartIntent(Context context, String artistId, String artistName) {
        Intent starter = new Intent(context, ShowTopTracksActivity.class);
        starter.putExtra(Constant.ID_ARTIST, artistId);
        starter.putExtra(Constant.NAME_ARTIST, artistName);
        return starter;
    }

    // anim view before close
    @Override
    public void onBackPressed() {
        mShowTopTrackView.expandImageAndFinish();
    }

    // want to use custome font
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
        final String artistId = getIntent().getStringExtra(Constant.ID_ARTIST);
        final String nameArtist = getIntent().getStringExtra(Constant.NAME_ARTIST);

        // create view
        mShowTopTrackView =
                (ShowTopTracksFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_SHOW_TOP_TRACKS);
        if (mShowTopTrackView == null) {
            // Create the fragment
            mShowTopTrackView = ShowTopTracksFragment.newInstance(artistId, nameArtist);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mShowTopTrackView, R.id.contentFrame);
        }

        // Create the presenter
        mShowTopTracksPresenter = new ShowTopTracksPresenter(mShowTopTrackView);
    }


}
