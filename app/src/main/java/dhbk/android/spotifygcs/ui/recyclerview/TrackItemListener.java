package dhbk.android.spotifygcs.ui.recyclerview;

import dhbk.android.spotifygcs.domain.TopTrack;

/**
 * Created by phongdth.ky on 7/20/2016.
 * A simple interface to be configured in a TopTracksActivity
 * define method when interact with a track
 */
public interface TrackItemListener {
    void onTrackClick(TopTrack topTrack, int position);
}
