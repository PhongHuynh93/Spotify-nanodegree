package dhbk.android.spotifygcs.module;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.io.SpotifyApiService;

/**
 * Created by phongdth.ky on 7/15/2016.
 * this module used to communicate the service with network.
 */
@Module
public class InteractorModule {
    @Provides
    public SpotifyInteractor provideArtistSearchInteractor(SpotifyApiService apiService) {
        return new SpotifyInteractor(apiService);
    }
}
