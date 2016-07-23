package dhbk.android.spotifygcs.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.MVPApp;
import dhbk.android.spotifygcs.io.SpotifyApiAdapter;
import dhbk.android.spotifygcs.io.SpotifyApiService;
import retrofit2.Retrofit;

/**
 * Created by phongdth.ky on 7/15/2016.
 * contains all retrofit instance and services need for connecting and downloading a list of artists
 * contains: application context,
 * retrofit instance,
 * retrofit service to download a list of artists from spotify
 */
@Module
public class SpotifyStreamerModule {
    private MVPApp app;

    public SpotifyStreamerModule(MVPApp app) {
        this.app = app;
    }

//    // return application context
//    @Provides
//    @Singleton
//    public Application provideApplication() {
//        return app;
//    }

    @Provides
    @Singleton
    public Context provideContext() {
        return app;
    }

    // To send network requests to an API, we need to use the Retrofit instance
    @Provides
    @Singleton
    public Retrofit provideRetrofitInstance() {
        return SpotifyApiAdapter.getInstance();
    }

    // the para is the retrofit instance above
    // the return is the artist models define in SpotifyApiService
    @Provides
    @Singleton
    public SpotifyApiService provideSpotifyApiService(Retrofit retrofit) {
        return retrofit.create(SpotifyApiService.class);
    }
}
