package dhbk.android.spotifygcs.module;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.io.SpotifyApiService;

/**
 * Created by phongdth.ky on 7/15/2016.
 * supply {@link SpotifyInteractor} for views to interact with api spotify server.
 */
@Module
public class InteractorModule {

    /**
     * @param apiService from {@link SpotifyStreamerModule}
     * @return
     */
    @Provides
    public SpotifyInteractor provideArtistSearchInteractor(SpotifyApiService apiService) {
        return new SpotifyInteractor(apiService);
    }
}
