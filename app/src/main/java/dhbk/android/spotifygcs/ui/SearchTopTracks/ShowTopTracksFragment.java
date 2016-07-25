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
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.ui.fab.FABRevealLayout;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.recyclerview.TrackItemListener;
import dhbk.android.spotifygcs.ui.searchArtist.SearchResultsFragment;
import dhbk.android.spotifygcs.ui.widget.ElasticDragDismissFrameLayout;
import dhbk.android.spotifygcs.ui.widget.ParallaxScrimageView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.HelpUtil;

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
    private ElasticDragDismissFrameLayout.SystemChromeFader chromeFader;
    private boolean firstTimeClick = true;
    private int trackDuration;
    private SpotifyPlayerService spotifyPlayerService;
    private boolean isServiceBounded = false;
    private boolean isPlayerPlaying = false;
    // track position in list
    private int trackPositionInList = 0;
    // time of music when pause the music
    private int trackCurrentPosition = 0;
    private Intent spotifyServiceIntent = null; // old intent

    private final Handler playerHandler = new Handler() {
        // receiver message from service to change song
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (trackDuration == 0) {
                setTrackDuration();
            }

            trackCurrentPosition = msg.getData().getInt(SpotifyPlayerService.CURRENT_TRACK_POSITION);
            mSeekbarShowtrackMusic.setProgress(trackCurrentPosition);
            mTextviewTimeSongPlayingNow.setText("00:" + String.format("%02d", trackCurrentPosition));

            // reset field, change icon when click play or stop.
            if (mPresenter != null) {
                if (trackCurrentPosition == trackDuration && trackCurrentPosition != 0) {
                    mPresenter.resetPlayer();
                }

                if (isPlayerPlaying) {
                    mPresenter.changeIconToPlay();
                } else {
                    mPresenter.changeIconToStop();
                }
            }

        }
    };
    private boolean isPlayerPaused = false;
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
    // connection to connect to play music service
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            SpotifyPlayerService.Binder playerBinder = (SpotifyPlayerService.Binder) iBinder;
            spotifyPlayerService = playerBinder.getService();
            isServiceBounded = true;
            if (!isPlayerPlaying) {
                isPlayerPlaying = true;
            }

            setTrackDuration();
            spotifyPlayerService.setSpotifyPlayerHandler(playerHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBounded = false;
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

    // reset the player, stop playing or pause, refresh duration
    @Override
    public void resetPlayer() {
        isPlayerPlaying = false;
        isPlayerPaused = false;
        trackCurrentPosition = 0;
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
        mPresenter.stopDoBackgroundThread();
        mPresenter = null;
        destroySpotifyService();
    }

    @Override
    public void destroySpotifyService() {
        if (spotifyPlayerService != null) {
            spotifyPlayerService.noUpdateUI();
            if (isServiceBounded) {
                getActivity().getApplicationContext().unbindService(serviceConnection);
                isServiceBounded = false;
            }
        }
        if (!isPlayerPaused && !isPlayerPlaying) {
            getActivity().getApplicationContext().stopService(new Intent(getActivity(), SpotifyPlayerService.class));
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

    //    start service with url of a song which was clicked
    @Override
    public void startSpotifyService(TopTrack topTrack, int trackPositionInList) {
        // if we click, the same track in a list, dont start again that service
        if (this.trackPositionInList == trackPositionInList) {
            // if the first time we click a track, start music to play
            if (!HelpUtil.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
                // TODO: 7/25/16 change to use presenter
                trackCurrentPosition = 0;
                getActivity().getApplicationContext().startService(spotifyServiceIntent);
            }
            // if we click the track again, do nothing
        } // if not click the same track, start a new service and play it
        else {
            Intent spotifyServiceIntent = SpotifyPlayerService.createStartIntent(getActivity(), topTrack.getTrackUrl());

            if (HelpUtil.isServiceRunning(SpotifyPlayerService.class, getActivity())) {
                trackCurrentPosition = 0;
                // stop old service to start a new one.
                getActivity().getApplicationContext().stopService(this.spotifyServiceIntent);
                getActivity().getApplicationContext().startService(spotifyServiceIntent);
                this.spotifyServiceIntent = spotifyServiceIntent;
            } else {
                trackCurrentPosition = 0;
                getActivity().getApplicationContext().startService(spotifyServiceIntent);
            }
        }

        // set position to the new one
        this.trackPositionInList = trackPositionInList;

        // bound service to component
        if (spotifyPlayerService == null) {
            getActivity().getApplication().bindService(spotifyServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        }

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
                        getActivity().finishAfterTransition();
                    }
                });
                expandImage.start();
            } else {
                getActivity().finishAfterTransition();
            }
        }

    }


    private void setClickListener() {
        mTopTrackAdapter.setClickListenerInterface(this);
    }

    // a callback with results is the toptrack which was clicked.
    @Override
    public void onTrackClick(TopTrack topTrack, int position) {
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
        startSpotifyService(topTrack, position);

        // show new player  (play - stop - pause)
        mTextviewToptrackNameOfArtist.setText(topTrack.getArtistsOfTrack());
        mTextviewToptrackNameOfSong.setText(topTrack.getNameOfTrack());
        mFabRevealLayout.revealSecondaryView();

        // play music
    }

    @OnClick({R.id.imagebutton_top_track_previous, R.id.imagebutton_top_track_play, R.id.imagebutton_top_track_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imagebutton_top_track_previous:
                break;
            case R.id.imagebutton_top_track_play:
                if (isPlayerPlaying) {
                    mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_play);
                    spotifyPlayerService.pauseTrack();
                    isPlayerPaused = true;
                    isPlayerPlaying = false;
                } else {
                    mImagebuttonTopTrackPlay.setImageResource(android.R.drawable.ic_media_pause);
                    spotifyPlayerService.playTrack(trackCurrentPosition);
                    isPlayerPaused = true;
                    isPlayerPlaying = true;
                }
                break;
            case R.id.imagebutton_top_track_next:
                break;
        }
    }

    // set the text to the time that player is playing
    @Override
    public void setTrackDuration() {
        if (spotifyPlayerService != null) {
            trackDuration = spotifyPlayerService.getTrackDuration();
            // the first time, trackDuration = 0, we the seekbar set to 0
            mSeekbarShowtrackMusic.setMax(trackDuration);
            // but a few second later after we connect to api server trackDuration to set to the length of song , trackDuration has the value different from 0
            mTextviewTimeLengthOfSong.setText(spotifyPlayerService.getTrackDurationString());
        }
    }
}
