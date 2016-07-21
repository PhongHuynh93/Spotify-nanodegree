package dhbk.android.spotifygcs.ui.recyclerview;

import android.view.MotionEvent;
import android.view.View;

import dhbk.android.spotifygcs.domain.Artist;

/**
 * Created by phongdth.ky on 7/20/2016.
 * A simple interface to be configured in a SearchResultsActivity
 */
public interface ArtistItemListener {
    void onArtistClick(Artist clickedTask, View v);
    boolean onArtistTouch (View view, MotionEvent motionEvent);
//
//    void onCompleteTaskClick(Task completedTask);
//
//    void onActivateTaskClick(Task activatedTask);
}
