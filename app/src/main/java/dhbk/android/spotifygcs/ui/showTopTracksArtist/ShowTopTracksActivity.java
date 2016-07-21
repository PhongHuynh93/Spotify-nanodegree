package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.content.Context;
import android.content.Intent;

import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.Constant;

public class ShowTopTracksActivity extends BaseActivity {
    private ShowTopTracksPresenter mShowTopTracksPresenter;
    private ShowTopTracksFragment mShowTopTrackView;

    public static Intent createStartIntent(Context context, String artistId, String urlLargeImage) {
        Intent starter = new Intent(context, ShowTopTracksActivity.class);
        starter.putExtra(Constant.ID_ARTIST, artistId);
        starter.putExtra(Constant.URL, urlLargeImage);
        return starter;
    }

    // anim view before close
    @Override
    protected void doWhenPressBackButton() {
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
        final String urlLargeImage = getIntent().getStringExtra(Constant.URL);

        // create view
        mShowTopTrackView =
                (ShowTopTracksFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_SHOW_TOP_TRACKS);
        if (mShowTopTrackView == null) {
            // Create the fragment
            mShowTopTrackView = ShowTopTracksFragment.newInstance(artistId, urlLargeImage);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), mShowTopTrackView, R.id.contentFrame);
        }

        // Create the presenter
        mShowTopTracksPresenter = new ShowTopTracksPresenter(mShowTopTrackView);
    }


}
