package dhbk.android.spotifygcs.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phongdth.ky on 7/15/2016.
 * a child model for images of an {@link Artist}
 * see field here {@link SpotifyConstant}
 * * See for more field from JSON:
 * @see <a href="https://api.spotify.com/v1/search?q=hari&type=artist">https://api.spotify.com</a>
 */
public class ArtistImage {
    @SerializedName(SpotifyConstant.WIDTH)
    int width;

    @SerializedName(SpotifyConstant.HEIGHT)
    int height;

    @SerializedName(SpotifyConstant.URL)
    String url;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public String getUrl() {
        return url;
    }
}
