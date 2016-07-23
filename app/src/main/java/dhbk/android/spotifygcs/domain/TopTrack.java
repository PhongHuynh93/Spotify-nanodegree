package dhbk.android.spotifygcs.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dhbk.android.spotifygcs.util.HelpUtil;

/**
 * Created by phongdth.ky on 7/20/2016.
 * See API Endpoint Reference Artists:
 * @ see <a href="https://developer.spotify.com/web-api/get-artists-top-tracks/">Get an Artist’s Top Tracks</a>
 * @ see <a href="https://api.spotify.com/v1/artists/43ZHCT0cAZBISjO8DG9PnE/top-tracks?country=SE">See field in JSON</a>
 */
public class TopTrack {
    @SerializedName(SpotifyConstant.NAME_OF_TRACKS)
    String mNameOfTrack;

    // is milisecond
    @SerializedName(SpotifyConstant.DURATION_OF_TRACKS)
    int mDurationOfTrack;

    // the url that play track for 30s
    @SerializedName(SpotifyConstant.TRACK_REVIEW_URL)
    String trackUrl;

    // a array of artists which sings this track
    @SerializedName(SpotifyConstant.ARTISTS_OF_TRACKS)
    ArrayList<Artist> mArtistsOfTrack;

    public String getNameOfTrack() {
        return mNameOfTrack;
    }

    // return in type "minute:second"
    public String getDurationOfTrack() {
        return HelpUtil.transformMilisecond(mDurationOfTrack);
    }

    // change from array list to string
    public String getArtistsOfTrack() {
        String allArtistsInTrack = "";
        for (Artist artist : mArtistsOfTrack) {
            allArtistsInTrack += artist.getNameArtist();
        }
        return allArtistsInTrack;
    }

    // need url of track for listening from it.
    public String getTrackUrl() {
        return trackUrl;
    }
}
