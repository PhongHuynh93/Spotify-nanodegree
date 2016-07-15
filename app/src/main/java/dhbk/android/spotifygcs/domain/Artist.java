package dhbk.android.spotifygcs.domain;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import dhbk.android.spotifygcs.util.Constant;

/**
 * Created by phongdth.ky on 7/15/2016.
 * a model JSON for an artist
 */
public class Artist {
    // image url of an artist can be null, so check first
    // an artist may have many images, so add to array list
    @SerializedName(Constant.IMAGES)
    @Nullable
    SpotifyImage[] urlImage;
}
