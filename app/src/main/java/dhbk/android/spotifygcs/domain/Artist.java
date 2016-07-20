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
    @SerializedName(Constant.IMAGES)
    @Nullable
    SpotifyImage[] urlImage;

    @SerializedName(Constant.NAME_ARTIST)
    String nameArtist;

    // if on artist have many image, image at position 1 is medium 300 x 300 pixel (other is large or small)
    @Nullable
    public SpotifyImage getMediumImage() {
        assert urlImage != null;
        if (urlImage.length >= 2) {
            return urlImage[1];
        } else if (urlImage.length == 1) {
            return urlImage[0];
        }
        return null;
    }

    public String getNameArtist() {
        return nameArtist;
    }
}
