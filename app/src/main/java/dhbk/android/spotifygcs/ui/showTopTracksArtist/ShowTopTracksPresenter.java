package dhbk.android.spotifygcs.ui.showTopTracksArtist;

import android.support.annotation.NonNull;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by phongdth.ky on 7/20/2016.
 */
public class ShowTopTracksPresenter implements ShowTopTracksContract.Presenter{
    private final ShowTopTracksContract.View mShowTopTrackView;

    public ShowTopTracksPresenter(@NonNull ShowTopTracksContract.View showTopTrackView) {
        mShowTopTrackView = checkNotNull(showTopTrackView, "tasksView cannot be null!");
        mShowTopTrackView.setPresenter(this);
    }


    // works when views start
    @Override
    public void start() {

    }
}
