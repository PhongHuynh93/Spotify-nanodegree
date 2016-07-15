package dhbk.android.spotifygcs.interactor;

import dhbk.android.spotifygcs.io.SpotifyApiService;
import dhbk.android.spotifygcs.io.callback.ArtistSearchServerCallback;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by phongdth.ky on 7/15/2016.
 * contain methods to interact with network
 */
public class ArtistSearchInteractor {
    private final SpotifyApiService mApiService;

    public ArtistSearchInteractor(SpotifyApiService apiService) {
        mApiService = apiService;
    }


    // use api service to perform a network call to api using rxjava and lambda expression
    public void performSearch(String query, ArtistSearchServerCallback callback) {
        mApiService.searchArtist(query)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artistSearchResponse -> {
                            callback.onArtistsFound(artistSearchResponse.getArtists());
                        }
                        , throwable -> {
                            callback.onFailedSearch();
                        });
    }
}
