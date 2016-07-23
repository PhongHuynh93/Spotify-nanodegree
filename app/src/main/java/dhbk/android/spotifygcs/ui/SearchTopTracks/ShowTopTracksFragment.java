package dhbk.android.spotifygcs.ui.SearchTopTracks;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.ui.SearchArtist.SearchResultsFragment;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.recyclerview.TrackItemListener;
import dhbk.android.spotifygcs.ui.widget.ElasticDragDismissFrameLayout;
import dhbk.android.spotifygcs.ui.widget.ParallaxScrimageView;
import dhbk.android.spotifygcs.util.AnimUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowTopTracksFragment extends BaseFragment implements
        ShowTopTracksContract.View,
        TrackItemListener {
    private static final String ARG_ARTIST_ID = "artist_id";
    private static final String ARG_ARTIST_NAME = "artist_name";

    @BindView(R.id.shot)
    ParallaxScrimageView mImageviewShowArtist;
    @BindView(R.id.back)
    ImageButton mBack;
    @BindView(R.id.recyclerview_show_top_track)
    RecyclerView mRecyclerviewShowTopTrack;
    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout mDraggableFrame;
    @Inject
    SpotifyInteractor mSpotifyInteractor;
    @Inject
    TopTrackAdapter mTopTrackAdapter;
    @BindView(R.id.fab_heart)
    FloatingActionButton mFabHeart;
    private String mArtistId;
    private String mArtistName;
    private ShowTopTracksContract.Presenter mPresenter;
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;

    private Transition.TransitionListener shotReturnHomeListener =
            new AnimUtils.TransitionListenerAdapter() {
                @Override
                public void onTransitionStart(Transition transition) {
                    super.onTransitionStart(transition);
                    mFabHeart.setVisibility(View.INVISIBLE);
                    // fade out the "toolbar" & list as we don't want them to be visible during return
                    mBack.animate()
                            .alpha(0f)
                            .setDuration(100)
                            .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()));
                    mImageviewShowArtist.setElevation(1f);
                    mBack.setElevation(0f);
                    mRecyclerviewShowTopTrack.animate()
                            .alpha(0f)
                            .setDuration(50)
                            .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()));
                }
            };

    public ShowTopTracksFragment() {
    }

    //    id used to construct top track api
    public static ShowTopTracksFragment newInstance(String artistId, String artistName) {
        ShowTopTracksFragment showTopTracksFragment = new ShowTopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistId);
        args.putString(ARG_ARTIST_NAME, artistName);
        showTopTracksFragment.setArguments(args);
        return showTopTracksFragment;
    }

    @Override
    protected void doThingWhenResumeApp() {
        mDraggableFrame.addListener(chromeFader);
    }

    @Override
    protected void doThingWhenPauseApp() {
        mDraggableFrame.removeListener(chromeFader);
    }

    @Override
    protected void doThingWhenDestroyApp() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_show_top_tracks;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {
        appComponent.topTrackComponent(new TopTrackModule(this)).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(ShowTopTracksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        // getting the argument
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            mArtistName = getArguments().getString(ARG_ARTIST_NAME);
        }

        mImageviewShowArtist.setImageDrawable(SearchResultsFragment.sDrawable);
        SearchResultsFragment.sDrawable = null; // after set image, set it to null to recycle
        mBack.setOnClickListener(v -> expandImageAndFinish());

        // anim
        getActivity().getWindow().getSharedElementReturnTransition().addListener(shotReturnHomeListener);

        // when drag image, close views
        chromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(getActivity()) {
            @Override
            public void onDragDismissed() {
                expandImageAndFinish();
            }
        };
    }

    public SpotifyInteractor getSpotifyInteractor() {
        checkNotNull(mSpotifyInteractor, "SpotifyInteractor cannot be null");
        return mSpotifyInteractor;
    }

    @Override
    public void setupRecyclerView() {
        mRecyclerviewShowTopTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerviewShowTopTrack.setHasFixedSize(true);
    }

    @Override
    public void setupAdapter() {
        checkNotNull(mTopTrackAdapter, "adapter not be null before set to list");
        mRecyclerviewShowTopTrack.setAdapter(mTopTrackAdapter);
        setClickListener();
        mRecyclerviewShowTopTrack.setItemAnimator(new SlideInItemAnimator());
    }

    @Override
    public String getIdArtist() {
        return mArtistId;
    }

    @Override
    public void showTrackOnList(ArrayList<TopTrack> topTracks) {
        mTopTrackAdapter.addAll(topTracks);
    }

    // show anim before close view
    @Override
    public void expandImageAndFinish() {
//        return OK status to calling view
//        final Intent resultData = new Intent();
//        resultData.putExtra(RESULT_EXTRA_SHOT_ID, shot.id);
//        setResult(RESULT_OK, resultData);
        if (mImageviewShowArtist.getOffset() != 0f) {
            Animator expandImage = ObjectAnimator.ofFloat(mImageviewShowArtist, ParallaxScrimageView.OFFSET,
                    0f);
            expandImage.setDuration(80);
            expandImage.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()));
            expandImage.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    getActivity().finishAfterTransition();
                }
            });
            expandImage.start();
        } else {
            getActivity().finishAfterTransition();
        }
    }


    private void setClickListener() {
        mTopTrackAdapter.setClickListenerInterface(this);
    }

    // a callback with results is the toptrack which was clicked.
    @Override
    public void onTrackClick(TopTrack topTrack) {
        // TODO: 7/21/2016 when click a track, show a play button depend on plaid
//        // start playing music, pass the toptrack url to a service
//        Intent spotifyServiceIntent = new Intent(getActivity(), SpotifyPlayerService.class);
//        spotifyServiceIntent.putExtra(Constant.TRACK_REVIEW_URL, topTrack.getTrackUrl());


//        if (Utils.isServiceRunning(SpotifyPlayerService.class, getActivity()) && !isPlayerPlaying) {
//            trackCurrentPosition = 0;
//            getActivity().getApplicationContext().stopService(spotifyServiceIntent);
//            getActivity().getApplicationContext().startService(spotifyServiceIntent);
//        } else if (!Utils.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
//            trackCurrentPosition = 0;
//            getActivity().getApplicationContext().startService(spotifyServiceIntent);
//        }
//        if (spotifyPlayerService == null) {
//            Log.d(SpotifyPlayerFragment.class.getSimpleName(), "" + isServiceBounded);
//            getActivity().getApplicationContext().bindService(spotifyServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
//        }
    }
}
