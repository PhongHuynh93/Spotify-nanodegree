package dhbk.android.spotifygcs.io.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.io.callback.ArtistSearchServerCallback;

/**
 * Created by phongdth.ky on 7/15/2016.
 * model of artists reponse from api
 * See more field in artist search {@link SpotifyResponseConstant}
 * used in {@link SpotifyInteractor}
 */
public class ArtistSearchResponse {
    @SerializedName(SpotifyResponseConstant.ARTISTS)
    ArtistsResponse artistsResponse;

    /**
     * this method is for {@link SpotifyInteractor#performArtistsSearch(String, ArtistSearchServerCallback)} to return a list of artist when search successes
     */
    public ArrayList<Artist> getArtists() {
        return artistsResponse.artists;
    }

    private class ArtistsResponse {
        @SerializedName(SpotifyResponseConstant.ARTIST_LISTS)
        ArrayList<Artist> artists;
    }
}
