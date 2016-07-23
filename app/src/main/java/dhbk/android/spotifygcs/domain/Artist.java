package dhbk.android.spotifygcs.domain;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phongdth.ky on 7/15/2016.
 * a model JSON for an artist
 * see field here {@link SpotifyConstant}
 * add another field here if you want to show more than image of artist
 */
public class Artist {
    // image url of an artist can be null, so check first
    private static final int LARGE_IMAGE = 0; // position 0 is a large image.
    @SerializedName(SpotifyConstant.IMAGES)
    @Nullable
    ArtistImage[] urlImage; // an artist may have many images, so add to array list
    @SerializedName(SpotifyConstant.NAME_ARTIST)
    String nameArtist;
    @SerializedName(SpotifyConstant.ID_ARTIST)
    String idArtist;

    @Nullable
    public ArtistImage getMediumImage() {
        assert urlImage != null;
        // there are many image from JSON, we choose to use the large one
        if (urlImage.length >= 1) {
            return urlImage[LARGE_IMAGE];
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
