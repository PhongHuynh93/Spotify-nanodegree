package dhbk.android.spotifygcs.ui.SearchTopTracks;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.ui.SearchArtist.SearchResultsFragment;
import dhbk.android.spotifygcs.ui.fab.FABRevealLayout;
import dhbk.android.spotifygcs.ui.recyclerview.DividerItemDecoration;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.recyclerview.TrackItemListener;
import dhbk.android.spotifygcs.ui.widget.ElasticDragDismissFrameLayout;
import dhbk.android.spotifygcs.ui.widget.ParallaxScrimageView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.HelpUtil;

import static com.google.common.base.Preconditions.checkNotNull;

// TODO: 7/25/2016 show a notification when app was playing, because we cant stop the service when distroy this view.
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
    @Named("bottom")
    TopTrackAdapter mTopTrackAdapter;
    @Inject
    @Named("right")
    TopTrackAdapter mTopTrackAdapterNav;
    @BindView(R.id.fab_heart)
    FloatingActionButton mFabHeart;
    @BindView(R.id.textview_toptrack_name_of_song)
    TextView mTextviewToptrackNameOfSong;
    @BindView(R.id.textview_toptrack_name_of_artist)
    TextView mTextviewToptrackNameOfArtist;
    @BindView(R.id.imagebutton_top_track_previous)
    ImageButton mImagebuttonTopTrackPrevious;
    @BindView(R.id.imagebutton_top_track_play)
    ImageButton mImagebuttonTopTrackPlay;
    @BindView(R.id.imagebutton_top_track_next)
    ImageButton mImagebuttonTopTrackNext;
    @BindView(R.id.textview_time_song_playing_now)
    TextView mTextviewTimeSongPlayingNow;
    @BindView(R.id.seekbar_showtrack_music)
    SeekBar mSeekbarShowtrackMusic;
    @BindView(R.id.textview_time_length_of_song)
    TextView mTextviewTimeLengthOfSong;

    private RecyclerView mRecyclerviewShowTopTrackNavRight;
    /**
     * see this library
     *
     * @see <a href="https://github.com/truizlop/FABRevealLayout"></a>
     */
    @BindView(R.id.fab_reveal_layout)
    FABRevealLayout mFabRevealLayout;
    private String mArtistId;
    private String mArtistName;
    private ShowTopTracksContract.Presenter mPresenter;
    private ElasticDragDismissFrameLayout.SystemChromeFader mChromeFader;
    private SpotifyPlayerService mSpotifyPlayerService;
    private boolean mIsServiceBounded = false;
    private boolean mIsPlayerPlaying = false;
    private boolean mIsPlayerPaused = false;
    // track position in list
    private int mTrackPositionInList = 0;
    // time of music when pause the music
    private int mTrackCurrentPosition = 0;
    // the length of the music track
    private int mTrackDuration = 0;

    /**
     * Handler that receives messages from the {@link SpotifyPlayerService}
     */
    private final Handler playerHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mTrackDuration == 0) {
                setTrackDuration();
            }

            mTrackCurrentPosition = msg.getData().getInt(SpotifyPlayerService.CURRENT_TRACK_POSITION);
            mSeekbarShowtrackMusic.setProgress(mTrackCurrentPosition);
            mTextviewTimeSongPlayingNow.setText("00:" + String.format("%02d", mTrackCurrentPosition));

            // reset field, change icon when click play or stop.
            if (mPresenter != null) {
                if (mTrackCurrentPosition == mTrackDuration && mTrackCurrentPosition != 0) {
                    mPresenter.resetPlayer();
                }

                if (mIsPlayerPlaying) {
                    mPresenter.changeIconToPlay();
                } else {
                    mPresenter.changeIconToStop();
                }
            }

        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SpotifyPlayerService.Binder playerBinder = (SpotifyPlayerService.Binder) iBinder;
            mSpotifyPlayerService = playerBinder.getService();
            mIsServiceBounded = true;
            if (!mIsPlayerPlaying) {
                mIsPlayerPlaying = true;
            }

            setTrackDuration();
            mSpotifyPlayerService.setSpotifyPlayerHandler(playerHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mIsServiceBounded = false;

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
    protected void initView() {
        mPresenter.initView();
    }

    @Override
    public void setViewObject() {
        /**
         *         getting the argument {@link ShowTopTracksFragment#newInstance(String, String)}
         */
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            mArtistName = getArguments().getString(ARG_ARTIST_NAME);
        }

        // set recyclerview for right navigation drawer
        mRecyclerviewShowTopTrackNavRight = (RecyclerView) getActivity().findViewById(R.id.recyclerview_list_track_nav);

        // set image of an artist
        mImageviewShowArtist.setImageDrawable(SearchResultsFragment.sDrawable);
        SearchResultsFragment.sDrawable = null; // after set image, set it to null to recycle


    }

    @Override
    public void addListener() {
        // anim when close the share preference
        getActivity().getWindow().getSharedElementReturnTransition().addListener(new AnimUtils.TransitionListenerAdapter() {
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
        });
        // when drag image, close views
        mBack.setOnClickListener(v -> expandImageAndFinish());
        mChromeFader = new ElasticDragDismissFrameLayout.SystemChromeFader(getActivity()) {
            // when we stop dragging, close this view
            @Override
            public void onDragDismissed() {
                expandImageAndFinish();
            }
        };
    }


    // show anim before close view
    @Override
    public void expandImageAndFinish() {
        // if second view (the view with play setting) is opened, closed it and return to lists of track
        if (!mFabRevealLayout.isShowingMainView()) {
            mFabRevealLayout.revealMainView();
        } // go to previous activity
        else {
            if (mImageviewShowArtist.getOffset() != 0f) {
                Animator expandImage = ObjectAnimator.ofFloat(mImageviewShowArtist, ParallaxScrimageView.OFFSET,
                        0f);
                expandImage.setDuration(80);
                expandImage.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()));
                expandImage.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // go back to revious view with the reverse transition
                        mPresenter.goBackToPreviousView();
                    }
                });
                expandImage.start();
            } else {
                // go back to revious view with the reverse transition
                mPresenter.goBackToPreviousView();
            }
        }
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
        checkNotNull(presenter);
        mPresenter = presenter;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    public void changeIcon(String icon) {
        new Handler(Looper.getMainLooper()).post(() -> {
            switch (icon) {
                case ShowTopTracksPresenter.PLAY_ICON:
                    mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_pause);
                    break;
                case ShowTopTracksPresenter.STOP_ICON:
                    mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_play);
                    break;
                default:
                    break;
            }
        });
    }

    // reset the player, stop playing or pause, refresh current position of the track
    @Override
    public void resetPlayer() {
        mIsPlayerPlaying = false;
        mIsPlayerPaused = false;
        mTrackCurrentPosition = 0;
    }

    @Override
    protected void doThingWhenResumeApp() {
        mDraggableFrame.addListener(mChromeFader);
    }

    @Override
    protected void doThingWhenPauseApp() {
        mDraggableFrame.removeListener(mChromeFader);
    }

    @Override
    protected void doThingWhenDestroyApp() {
        mPresenter.stopDoBackgroundThread();
        mPresenter = null;
        destroySpotifyService();
    }

    @Override
    public void destroySpotifyService() {
        if (mSpotifyPlayerService != null) {
//            when destroy service, unbind it first
            mSpotifyPlayerService.noUpdateUI();
            if (mIsServiceBounded) {
                getActivity().getApplicationContext().unbindService(mServiceConnection);
                mIsServiceBounded = false;
            }
        }

        // when no other tracks play, also kill the service
        if (!mIsPlayerPaused && !mIsPlayerPlaying) {
            getActivity().getApplicationContext().stopService(getIntentForService());
        }

//         when my app have a track playing or pausing, don't stop the service.
//        if a track is playing, keep playing
    }

    @Override
    public Intent getIntentForService() {
        return new Intent(getActivity(), SpotifyPlayerService.class);
    }

    @Override
    public Intent getIntentForService(String trackUrl) {
        return SpotifyPlayerService.createStartIntent(getActivity(), trackUrl);
    }


    // start service to play music
    @Override
    public void startServiceSpotify(Intent spotifyServiceIntent) {
        mTrackCurrentPosition = 0;
        getActivity().getApplicationContext().startService(spotifyServiceIntent);
    }

    // nav to new track, so stop old service and start new one
    @Override
    public void restartServiceSpotify(Intent spotifyServiceIntent) {
        mTrackCurrentPosition = 0;
        // stop running old music
        getActivity().getApplicationContext().stopService(spotifyServiceIntent);
        // running new music instead
        getActivity().getApplicationContext().startService(spotifyServiceIntent);
    }

    // nav to old service, so not stop the service
    @Override
    public void resumeServiceSpotify(Intent spotifyServiceIntent) {

    }

    public SpotifyInteractor getSpotifyInteractor() {
        return mSpotifyInteractor;
    }

    @Override
    public void setupRecyclerView() {
        mRecyclerviewShowTopTrack.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerviewShowTopTrack.setHasFixedSize(true);

        mRecyclerviewShowTopTrackNavRight.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerviewShowTopTrackNavRight.setHasFixedSize(true);
    }

    @Override
    public void setupAdapter() {
//        bottom list
        checkNotNull(mTopTrackAdapter, "adapter not be null before set to list");
        mRecyclerviewShowTopTrack.setAdapter(mTopTrackAdapter);
        mTopTrackAdapter.setClickListenerInterface(this);
        mRecyclerviewShowTopTrack.setItemAnimator(new SlideInItemAnimator());

//        right nav list
        checkNotNull(mTopTrackAdapterNav, "adapter not be null before set to list");
        mRecyclerviewShowTopTrackNavRight.setAdapter(mTopTrackAdapterNav);
        mTopTrackAdapterNav.setClickListenerInterface(this);
        mRecyclerviewShowTopTrackNavRight.setItemAnimator(new SlideInItemAnimator());

        // add horizontal white divider between each row
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST, R.color.grey_light2);
        mRecyclerviewShowTopTrack.addItemDecoration(itemDecoration);
        mRecyclerviewShowTopTrackNavRight.addItemDecoration(itemDecoration);
    }

    @Override
    public String getIdArtist() {
        return mArtistId;
    }

    @Override
    public void showTrackOnList(ArrayList<TopTrack> topTracks) {
        mTopTrackAdapter.addAll(topTracks);
        mTopTrackAdapterNav.addAll(topTracks);
    }

    // anim before go to previous view
    @Override
    public void goToPreviousView() {
        getActivity().supportFinishAfterTransition();
    }


    // a callback with results is the toptrack which was clicked.
    @Override
    public void onTrackClick(TopTrack topTrack, int position) {
        // play music
        startSpotifyService(topTrack, position);

        // show new player  (play - stop - pause)
        mTextviewToptrackNameOfArtist.setText(topTrack.getArtistsOfTrack());
        mTextviewToptrackNameOfSong.setText(topTrack.getNameOfTrack());
        mFabRevealLayout.revealSecondaryView();
    }


    //   start service with url of a song which was clicked
    @Override
    public void startSpotifyService(TopTrack topTrack, int mTrackPositionInList) {
        // if we click, the same track in a list, dont start again that service
        Intent spotifyServiceIntent = getIntentForService(topTrack.getTrackUrl());
        if (this.mTrackPositionInList == mTrackPositionInList) {
            // if the first time we click a track, start music to play
            if (!HelpUtil.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
                // start to play track
                mPresenter.startServiceSpotify(spotifyServiceIntent);
            }  // if we click the track again
            else {
                // resume the old track
                mPresenter.resumeServiceSpotify(spotifyServiceIntent);
            }
        } // if not click the same track, start a new service and play it
        else {
            if (HelpUtil.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
                // stop old service to start a new one.
                mPresenter.restartServiceSpotify(spotifyServiceIntent);
            } else {
                // for the first time, we click track (not 0), so service is not running
                mPresenter.startServiceSpotify(spotifyServiceIntent);
            }
        }

        // set position to the new one
        this.mTrackPositionInList = mTrackPositionInList;

        // bound service to component for the first time the view loads, so this service = null
        if (mSpotifyPlayerService == null) {
            getActivity().getApplication().bindService(spotifyServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    }

    @OnClick({R.id.imagebutton_top_track_previous, R.id.imagebutton_top_track_play, R.id.imagebutton_top_track_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebutton_top_track_previous:
                mPresenter.playPreviousTrack();
                break;
//            this button has 2 methods, play and pause music
            case R.id.imagebutton_top_track_play:
                // pause music
                if (mIsPlayerPlaying) {
                    mPresenter.pauseTrack();
                }
                // play music
                else {
                    // FIXME: 7/25/16 error here when play music, not resume play but play the next song
                    mPresenter.playTrack();
                }
                break;
            case R.id.imagebutton_top_track_next:
                mPresenter.playNextTrack();
                break;
            default:
                break;
        }
    }


    @Override
    public void setPlayState() {
        mIsPlayerPlaying = true;
        mIsPlayerPaused = false;
    }

    @Override
    public int getPreviousTrackPosition() {
        int trackPositionPrevious = mTrackPositionInList - 1;
        if (trackPositionPrevious < 0) {
            trackPositionPrevious = mTopTrackAdapter.getItemCount() - 1;
        }
        return trackPositionPrevious;
    }

    @Override
    public void startToPlayTrack(int position) {
        onTrackClick(mTopTrackAdapter.getItemAtPosition(position), position);
    }

    @Override
    public void startToPlayTrack() {
        new Handler(Looper.getMainLooper()).post(() ->
        {
            mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_pause);
            mSpotifyPlayerService.playTrack(mTrackCurrentPosition);
        });

    }

    @Override
    public void setPauseState() {
        mIsPlayerPaused = true;
        mIsPlayerPlaying = false;
    }

    @Override
    public void startToPauseTrack() {
        new Handler(Looper.getMainLooper()).post(() ->
        {
            mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_play);
            mSpotifyPlayerService.pauseTrack();
        });
    }

    @Override
    public int getNextTrackPosition() {
        return (mTrackPositionInList + 1) % mTopTrackAdapter.getItemCount();
    }

    // set the text to the time that player is playing
    @Override
    public void setTrackDuration() {
        if (mSpotifyPlayerService != null) {
            mTrackDuration = mSpotifyPlayerService.getTrackDuration();
            // the first time, mTrackDuration = 0, we the seekbar set to 0
            mSeekbarShowtrackMusic.setMax(mTrackDuration);
            // but a few second later after we connect to api server mTrackDuration to set to the length of song , mTrackDuration has the value different from 0
            mTextviewTimeLengthOfSong.setText(mSpotifyPlayerService.getTrackDurationString());
        }
    }
}
