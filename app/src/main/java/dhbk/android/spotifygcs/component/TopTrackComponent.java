package dhbk.android.spotifygcs.component;

import dagger.Subcomponent;
import dhbk.android.spotifygcs.ActivityScope;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.ui.showTopTracksArtist.ShowTopTracksFragment;

/**
 * Created by huynhducthanhphong on 7/16/16.
 */
@Subcomponent(modules={TopTrackModule.class })
@ActivityScope
public interface TopTrackComponent {
    void inject(ShowTopTracksFragment frag);
}
