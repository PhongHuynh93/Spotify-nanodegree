package dhbk.android.spotifygcs;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.module.InteractorModule;
import dhbk.android.spotifygcs.module.SpotifyStreamerModule;

/**
 * Created by phongdth.ky on 7/15/2016.
 */

/**
 * Module:
 * spotify: contains all retrofit instance and services need for connecting and downloading a list of artists
 * InteractorModule: contains method to for service to connect to network.
 */
@Singleton
@Component(modules = {SpotifyStreamerModule.class, InteractorModule.class})
public interface SpotifyStreamerComponent {
    Context getContext();
    ArtistSearchInteractor getArtistSearchInteractor();
}
