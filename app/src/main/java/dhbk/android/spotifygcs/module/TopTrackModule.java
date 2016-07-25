package dhbk.android.spotifygcs.module;

import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.ActivityScope;
import dhbk.android.spotifygcs.ui.SearchTopTracks.ShowTopTracksContract;
import dhbk.android.spotifygcs.ui.SearchTopTracks.TopTrackAdapter;

/**
 * Created by huynhducthanhphong on 7/16/16.
 * provide adapter for view to add data to list
 */
@Module
public class TopTrackModule {

    private ShowTopTracksContract.View view;

    public TopTrackModule(ShowTopTracksContract.View view) {
        this.view = view;
    }

    // return this view which inject this module
    @Provides
    @ActivityScope
    public ShowTopTracksContract.View provideView() {
        return view;
    }

    // return adapter for this view, context from parent component
    // adapter for music player at the bottom
    @Provides
    @ActivityScope
    @Named("bottom")
    public TopTrackAdapter provideAdapterBottom(Context context) {
        return new TopTrackAdapter(context);
    }

    // return adapter for this view, context from parent component
    // adapter for music player at the right navigation drawer
    @Provides
    @ActivityScope
    @Named("right")
    public TopTrackAdapter provideAdapterRight(Context context) {
        return new TopTrackAdapter(context);
    }
}
