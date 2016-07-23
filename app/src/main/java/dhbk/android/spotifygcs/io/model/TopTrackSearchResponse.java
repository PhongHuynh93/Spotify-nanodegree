package dhbk.android.spotifygcs.io.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.io.callback.TopTrackSearchServerCallback;

/**
 * Created by phongdth.ky on 7/15/2016.
 * model of artists reponse from api
 *
 * See more field in top ten tracks search {@link SpotifyResponseConstant}
 *
 * used in {@link SpotifyInteractor}
 */
public class TopTrackSearchResponse {
    @SerializedName(SpotifyResponseConstant.TRACKS)
    ArrayList<TopTrack> topTracks;

    /**
     * this method is for {@link SpotifyInteractor#performTopTrackSearch(String, TopTrackSearchServerCallback)} to return a list of top tracks when search successes
     */
    public ArrayList<TopTrack> getTopTracksOfArtist() {
        return topTracks;
    }

}
