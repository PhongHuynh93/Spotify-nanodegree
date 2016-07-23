package dhbk.android.spotifygcs.io.model;

/**
 * Created by huynhducthanhphong on 7/23/16.
 * constant for response from server.
 */
class SpotifyResponseConstant {
    /**
     * See more field in artist search.
     *
     * @see <a href="https://api.spotify.com/v1/search?q=tania%20bowra&type=artist">Artist field</a>
     */
    public static final String ARTISTS = "artists";
    public static final String ARTIST_LISTS = "items";

    /**
     * See more field in top ten tracks search
     *
     * @see <a href="https://api.spotify.com/v1/artists/43ZHCT0cAZBISjO8DG9PnE/top-tracks?country=SE"></a>
     */
    public static final String TRACKS = "tracks";
}
