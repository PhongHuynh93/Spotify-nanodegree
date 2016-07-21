package dhbk.android.spotifygcs.domain;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import dhbk.android.spotifygcs.util.Constant;

/**
 * Created by phongdth.ky on 7/15/2016.
 * a model JSON for an artist
 * // add another field here if you want to show more than image of artist
 */
public class Artist {
    // image url of an artist can be null, so check first
    // an artist may have many images, so add to array list
    private static final int LARGE_IMAGE = 0; // position is a large image from spotify server
    @SerializedName(Constant.IMAGES)
    @Nullable
    SpotifyImage[] urlImage;

    @SerializedName(Constant.NAME_ARTIST)
    String nameArtist;

    @SerializedName(Constant.ID_ARTIST)
    String idArtist;


    @Nullable
    public SpotifyImage getMediumImage() {
        assert urlImage != null;
        if (urlImage.length >= 1) {
            return urlImage[LARGE_IMAGE];
        }
        return null;
    }

    // large image in  urlImage[0], we want to get the url of this image
    @Nullable
    public String getUrlLargeImage() {
        if (urlImage.length >= 1) {
            return urlImage[0].getUrl();
        }
        return null;
    }

    public String getIdArtist() {
        return idArtist;
    }

    public String getNameArtist() {
        return nameArtist;
    }
}
