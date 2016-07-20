package dhbk.android.spotifygcs.domain;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import dhbk.android.spotifygcs.util.Constant;
import dhbk.android.spotifygcs.util.HelpUtil;

/**
 * Created by phongdth.ky on 7/20/2016.
 */
public class TopTrack {
    @SerializedName(Constant.NAME_OF_TRACKS)
    String mNameOfTrack;

    // is milisecond
    @SerializedName(Constant.DURATION_OF_TRACKS)
    int mDurationOfTrack;

    // a array of artists which sings this track
    @SerializedName(Constant.ARTISTS_OF_TRACKS)
    ArrayList<Artist> mArtistsOfTrack;

    public String getNameOfTrack() {
        return mNameOfTrack;
    }

    // return in type "minute:second"
    public String getDurationOfTrack() {
        return HelpUtil.transformMilisecond(mDurationOfTrack);
    }

    public String getArtistsOfTrack() {

        String allArtistsInTrack = "";
        for (Artist artist : mArtistsOfTrack) {
            allArtistsInTrack += artist.getNameArtist();
        }

        return allArtistsInTrack;
    }
}
