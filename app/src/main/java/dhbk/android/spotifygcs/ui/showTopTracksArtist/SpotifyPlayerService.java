package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import dhbk.android.spotifygcs.BaseBinder;
import dhbk.android.spotifygcs.BaseService;
import dhbk.android.spotifygcs.util.Constant;

public class SpotifyPlayerService extends BaseService implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener{
    private String mTrackUrlPreview;

    public SpotifyPlayerService() {
    }

    @Override
    public IBinder getBinder() {
        return new Binder();
    }

    // when start service, first get the music url
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

    ////////////////////////////////////////////////////////////
//    MediaPlayer
    // Interface definition for a callback to be invoked when playback of a media source has completed.
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    // Interface definition of a callback to be invoked when there has been an error during an asynchronous operation
    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    //Interface definition for a callback to be invoked when the media source is ready for playback.
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

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
    private void setTrackUrlPreview(String trackUrlPreview) {
        mTrackUrlPreview = trackUrlPreview;
    }


    private void playTrack(int trackPosition){

    }
}
