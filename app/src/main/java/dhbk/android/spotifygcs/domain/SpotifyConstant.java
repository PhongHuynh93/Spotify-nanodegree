package dhbk.android.spotifygcs.domain;

/**
 * Created by huynhducthanhphong on 7/23/16.
 * See API Endpoint Reference Artists:
 * @ see <a href="https://developer.spotify.com/web-api/artist-endpoints/">Web API Artist Endpoints</a>
 */
// use default modifier because I dont want other class in another package use this.
public class SpotifyConstant {
    /**
     * See for more field from artist search JSON:
     *
     * @see <a href="https://api.spotify.com/v1/search?q=hari&type=artist">https://api.spotify.com</a>
     */
    //    artist
    public static final String IMAGES = "images";
    public static final String NAME_ARTIST = "name";
    public static final String ID_ARTIST = "id";

    // artist image
    public static final String WIDTH = "width";
    public static final String HEIGHT = "height";
    public static final String URL = "url";


    /**
     * See API Endpoint Reference Artists:
     * @ see <a href="https://developer.spotify.com/web-api/get-artists-top-tracks/">Get an Artist’s Top Tracks</a>
     * @ see <a href="https://api.spotify.com/v1/artists/43ZHCT0cAZBISjO8DG9PnE/top-tracks?country=SE">See field in JSON</a>
     */
    //    top track
    public static final String NAME_OF_TRACKS = "name";
    public static final String DURATION_OF_TRACKS = "duration_ms";
    public static final String ARTISTS_OF_TRACKS = "artists";
    public static final String TRACK_REVIEW_URL = "preview_url";

}
