package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.content.Context;
import android.content.Intent;
import android.transition.Transition;
import android.view.View;

import dhbk.android.spotifygcs.BaseActivity;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.util.ActivityUtils;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.Constant;

public class ShowTopTracksActivity extends BaseActivity {
    private ShowTopTracksPresenter mShowTopTracksPresenter;

    public static Intent createStartIntent(Context context, String artistId, String urlLargeImage) {
        Intent starter = new Intent(context, ShowTopTracksActivity.class);
        starter.putExtra(Constant.ID_ARTIST, artistId);
        starter.putExtra(Constant.URL, urlLargeImage);
        return starter;
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


        ShowTopTracksFragment showTopTracksFragment =
                (ShowTopTracksFragment) getSupportFragmentManager().findFragmentByTag(Constant.TAG_FRAGMENT_SHOW_TOP_TRACKS);
        if (showTopTracksFragment == null) {
            // Create the fragment
            showTopTracksFragment = ShowTopTracksFragment.newInstance(artistId, urlLargeImage);
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), showTopTracksFragment, R.id.contentFrame);
        }

        // Create the presenter
        mShowTopTracksPresenter = new ShowTopTracksPresenter(showTopTracksFragment);

        getWindow().getSharedElementReturnTransition().addListener(shotReturnHomeListener);
    }


    private Transition.TransitionListener shotReturnHomeListener =
            new AnimUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(Transition transition) {
                    super.onTransitionStart(transition);
//                    // hide the fab as for some reason it jumps position??  TODO work out why
//                    fab.setVisibility(View.INVISIBLE);
                    // fade out the "toolbar" & list as we don't want them to be visible during return
                    // animation
                    back.animate()
                            .alpha(0f)
                            .setDuration(100)
                            .setInterpolator(getLinearOutSlowInInterpolator(DribbbleShot.this));
                    imageView.setElevation(1f);
                    back.setElevation(0f);
                    commentsList.animate()
                            .alpha(0f)
                            .setDuration(50)
                            .setInterpolator(getLinearOutSlowInInterpolator(DribbbleShot.this));
                }
            };

}
