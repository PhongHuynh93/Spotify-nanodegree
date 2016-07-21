package dhbk.android.spotifygcs.ui.showTopTracksArtist;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.MVPApp;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.recyclerview.TrackItemListener;
import dhbk.android.spotifygcs.ui.widget.ElasticDragDismissFrameLayout;
import dhbk.android.spotifygcs.ui.widget.ParallaxScrimageView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowTopTracksFragment extends BaseFragment implements
        ShowTopTracksContract.View,
        TrackItemListener{
    private static final String ARG_ARTIST_ID = "artist_id";
    private static final String ARG_URL_IMAGE = "url_name";

    @BindView(R.id.imageview_show_artist)
    ParallaxScrimageView mImageviewShowArtist;
    @BindView(R.id.back)
    ImageButton mBack;
    @BindView(R.id.recyclerview_show_top_track)
    RecyclerView mRecyclerviewShowTopTrack;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.draggable_frame)
    ElasticDragDismissFrameLayout mDraggableFrame;

    private String mArtistId;
    private ShowTopTracksContract.Presenter mPresenter;

    @Inject
    ArtistSearchInteractor mArtistSearchInteractor;

    @Inject
    TopTrackAdapter mTopTrackAdapter;

    public ShowTopTracksFragment() {
    }

    public static ShowTopTracksFragment newInstance(String artistId, String urlLargeImage) {
        ShowTopTracksFragment showTopTracksFragment = new ShowTopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistId);
        args.putString(ARG_URL_IMAGE, urlLargeImage);
        showTopTracksFragment.setArguments(args);
        return showTopTracksFragment;
    }

    @Override
    protected void doThingWhenPauseApp() {

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
        // inject component (contain adapter) for this view
        MVPApp.getApp(getContext())
                .getSpotifyStreamerComponent()
                .topTrackComponent(new TopTrackModule(this))
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        // set image
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            String urlImage = getArguments().getString(ARG_URL_IMAGE);
            // set image
            ViewUtils.setImagePicasso(getContext(), urlImage, mImageviewShowArtist);
        }
        mBack.setOnClickListener(v -> expandImageAndFinish());

        // anim
        getActivity().getWindow().getSharedElementReturnTransition().addListener(shotReturnHomeListener);
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


    @Override
    public void setPresenter(ShowTopTracksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public ArtistSearchInteractor getArtistSearchInteractor() {
        checkNotNull(mArtistSearchInteractor, "ArtistSearchInteractor cannot be null");
        return mArtistSearchInteractor;
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


    // TODO: 7/20/16 implement this
    private void setClickListener() {
        mTopTrackAdapter.setClickListenerInterface(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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
