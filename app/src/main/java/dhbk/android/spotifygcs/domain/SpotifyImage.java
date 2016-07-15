package dhbk.android.spotifygcs.domain;

import com.google.gson.annotations.SerializedName;

import dhbk.android.spotifygcs.util.Constant;

/**
 * Created by phongdth.ky on 7/15/2016.
 * a model for images of an artist
 */
public class SpotifyImage {
    @SerializedName(Constant.WIDTH)
    int width;

    @SerializedName(Constant.HEIGHT)
    int height;

    @SerializedName(Constant.URL)
    String url;
}
