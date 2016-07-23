package dhbk.android.spotifygcs.interactor;

import dhbk.android.spotifygcs.io.SpotifyApiService;
import dhbk.android.spotifygcs.io.callback.ArtistSearchServerCallback;
import dhbk.android.spotifygcs.io.callback.TopTrackSearchServerCallback;
import dhbk.android.spotifygcs.ui.SearchArtist.SearchResultsPresenter;
import dhbk.android.spotifygcs.ui.SearchTopTracks.ShowTopTracksPresenter;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by phongdth.ky on 7/15/2016.
 * contain methods to interact with network
 * communicate with {@link SearchResultsPresenter} {@link ShowTopTracksPresenter}
 */
public class ArtistSearchInteractor {
    private final SpotifyApiService mApiService;

    public ArtistSearchInteractor(SpotifyApiService apiService) {
        mApiService = apiService;
    }


    // search a list of artist which equals to query
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

    // search a list of tracks of an artist
    public void performTopTrackSearch(String idArtist, TopTrackSearchServerCallback callback) {
        mApiService.searchTopTrack(idArtist)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(artistSearchResponse -> {
                            callback.onTopTracksFound(artistSearchResponse.getTopTracksOfArtist());
                        }
                        , throwable -> {
                            callback.onFailedSearch();
                        });
    }


}
