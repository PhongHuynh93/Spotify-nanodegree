package dhbk.android.spotifygcs.ui.SearchTopTracks;

import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import dhbk.android.spotifygcs.BaseBinder;
import dhbk.android.spotifygcs.BaseService;
import dhbk.android.spotifygcs.util.Constant;

public class SpotifyPlayerService extends BaseService implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener,
        ShowTopTracksContract.MusicService {
    public static final String IS_PLAYER_PLAYING = "is_player_playing";
    public static final String CURRENT_TRACK_POSITION = "current_track_position";
    public static final String TRACK_PREVIEW_URL = "track_preview_url";

    private String mTrackUrlPreview;
    private Handler spotifyPlayerHandler;
    private boolean isPlayerPaused;
    private MediaPlayer spotifyPlayer;
    private Timer uiUpdater;
    private int currentTrackPosition;

    public SpotifyPlayerService() {
    }

    @Override
    public IBinder getBinder() {
        return new Binder();
    }

    // when start service, first get the music url via intent
    @Override
    public void initService(Intent intent) {
        if (intent != null && intent.hasExtra(Constant.TRACK_REVIEW_URL)) {
            String trackUrl = intent.getStringExtra(Constant.TRACK_REVIEW_URL);
            if (trackUrl != null) {
                setTrackUrlPreview(intent.getStringExtra(Constant.TRACK_REVIEW_URL));
                playTrack(0);
            }
        }
    }

    @Override
    protected void doThingBeforeDestroyService() {
        if(uiUpdater != null){
            noUpdateUI();
        }
        if(spotifyPlayer != null){
            spotifyPlayer.release();
            spotifyPlayer = null;
        }
        if(spotifyPlayerHandler != null){
            spotifyPlayerHandler = null;
        }
    }

    ////////////////////////////////////////////////////////////
//    MediaPlayer
    // Interface definition for a callback to be invoked when playback of a media source has completed.
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Message completionMessage = new Message();
        Bundle completionBundle = new Bundle();
        completionBundle.putBoolean(IS_PLAYER_PLAYING, false);
        completionMessage.setData(completionBundle);
        if (spotifyPlayerHandler != null) {
            spotifyPlayerHandler.sendMessage(completionMessage);
        }
        noUpdateUI();
    }

    // Interface definition of a callback to be invoked when there has been an error during an asynchronous operation
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    //Interface definition for a callback to be invoked when the media source is ready for playback.
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mediaPlayer.start();
        if(currentTrackPosition != 0){
            mediaPlayer.seekTo(currentTrackPosition * 1000);
        }
        updateUI();
    }

    public class Binder extends BaseBinder<SpotifyPlayerService> {
        @Override
        public SpotifyPlayerService getService() {
            return super.getService();
        }

        @Override
        public void setService(SpotifyPlayerService service) {
            super.setService(service);
        }
    }

    // set url to field
    public void setTrackUrlPreview(String trackUrlPreview) {
        mTrackUrlPreview = trackUrlPreview;
    }

    @Override
    public void playTrack(int trackPosition) {
        currentTrackPosition = trackPosition;
        if(spotifyPlayer != null) {
            if(spotifyPlayer.isPlaying()){
                spotifyPlayer.stop();
            }
            spotifyPlayer.reset();
        }
        initSpotifyPlayer();
        isPlayerPaused = false;
    }

    @Override
    public void initSpotifyPlayer(){
        if(spotifyPlayer == null)
            spotifyPlayer = new MediaPlayer();
        spotifyPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            spotifyPlayer.setDataSource(mTrackUrlPreview);
            spotifyPlayer.prepareAsync();
            spotifyPlayer.setOnCompletionListener(SpotifyPlayerService.this);
            spotifyPlayer.setOnPreparedListener(SpotifyPlayerService.this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        spotifyPlayer.setOnErrorListener(SpotifyPlayerService.this);
    }


    @Override
    public void setSpotifyPlayerHandler(Handler spotifyPlayerHandler) {
        this.spotifyPlayerHandler = spotifyPlayerHandler;
        Message spotifyPlayerMessage = new Message();
        Bundle spotifyPlayerBundle;
        if (this.spotifyPlayerHandler != null && (isPlayerPaused || spotifyPlayer.isPlaying())) {
            spotifyPlayerBundle = getCurrentTrackPosition();
            if (!isPlayerPaused) {
                updateUI();
            } else {
                spotifyPlayerBundle.putBoolean(IS_PLAYER_PLAYING, false);
            }
            spotifyPlayerMessage.setData(spotifyPlayerBundle);
            if (this.spotifyPlayerHandler != null) {
                this.spotifyPlayerHandler.sendMessage(spotifyPlayerMessage);
            }
        }

    }

    @Override
    public Bundle getCurrentTrackPosition() {
        Bundle uiBundle = new Bundle();
        if (spotifyPlayer != null && (isPlayerPaused || spotifyPlayer.isPlaying())) {
            uiBundle.putBoolean(IS_PLAYER_PLAYING, true);
            int trackPosition = (int) Math.ceil((double) spotifyPlayer.getCurrentPosition() / 1000);
            uiBundle.putInt(CURRENT_TRACK_POSITION, trackPosition);
        }
        return uiBundle;
    }

    @Override
    public void updateUI(){
        uiUpdater = new Timer();
        uiUpdater.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendCurrentTrackPosition();
            }
        },0,1000);
    }

    @Override
    public void noUpdateUI() {
        if(uiUpdater != null){
            uiUpdater.cancel();
            uiUpdater.purge();
        }
    }


    private void sendCurrentTrackPosition(){
        Message positionMessage = new Message();
        positionMessage.setData(getCurrentTrackPosition());
        if(spotifyPlayerHandler != null){
            spotifyPlayerHandler.sendMessage(positionMessage);
        }
    }
}
