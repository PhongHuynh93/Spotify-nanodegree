package dhbk.android.spotifygcs.io.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dhbk.android.spotifygcs.domain.TopTrack;
import dhbk.android.spotifygcs.util.Constant;

/**
 * Created by phongdth.ky on 7/15/2016.
 * model of artists reponse from api
 * from this link: https://api.spotify.com/v1/search?q=hari%20won&type=artist
 * we can convert to gson
 */
public class TopTrackSearchResponse {
    @SerializedName(Constant.TRACKS)
    ArrayList<TopTrack> topTracks;

    public ArrayList<TopTrack> getTopTracksOfArtist() {
        return topTracks;
    }

}
