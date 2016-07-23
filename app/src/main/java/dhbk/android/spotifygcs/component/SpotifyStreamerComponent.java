package dhbk.android.spotifygcs.component;

import javax.inject.Singleton;

import dagger.Component;
import dhbk.android.spotifygcs.module.ArtistSearchModule;
import dhbk.android.spotifygcs.module.InteractorModule;
import dhbk.android.spotifygcs.module.SpotifyStreamerModule;
import dhbk.android.spotifygcs.module.TopTrackModule;

/**
 * Created by phongdth.ky on 7/15/2016.
 */

/**
 * This is a parent dependence:
 * contains module to
 * Module:
 * {@link SpotifyStreamerModule}
 * {@link InteractorModule}:
 */
@Singleton
@Component(modules = {SpotifyStreamerModule.class, InteractorModule.class})
public interface SpotifyStreamerComponent {
    ArtistSearchComponent artistSearchComponent(ArtistSearchModule artistSearchModule);
    TopTrackComponent topTrackComponent(TopTrackModule topTrackModule);
}