package dhbk.android.spotifygcs.io;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Constants value used for search from spotify api
 * @see <a href="https://developer.spotify.com/web-api/search-item/">Search for an Item
</a>
 */
class SpotifyRequestConstants {
    // base url for spotify api
    public static final String BASE_URL = "https://api.spotify.com";
    public static final String QUERY_TO_SEARCH = "q";
    public static final String ID_ARTIST = "id";
    //    search artist
    private static final String VERSION_PATH = "/v1";
    private static final String SEARCH_PATH = "/search";
    private static final String TYPE_QUERY = "type";
    private static final String ARTIST = "artist";
    //  example curl -X GET "https://api.spotify.com/v1/search?q=tania%20bowra&type=artist"
    public static final String ARTIST_SEARCH_URL = VERSION_PATH + SEARCH_PATH + "?"+ TYPE_QUERY + "=" + ARTIST;
    //    search top tracks
    private static final String SEARCH_TOP_TRACK_PATH = "/artists";
    private static final String TOP_TRACK = "/top-tracks";
    private static final String TYPE_QUERY_COUNTRY = "?country";
    private static final String COUNTRY = "US";
    //    GET https://api.spotify.com/v1/artists/{id}/top-tracks
    public static final String TOP_TRACK_SEARCH_URL = VERSION_PATH + SEARCH_TOP_TRACK_PATH + "/{" + ID_ARTIST + "}" + TOP_TRACK
            + TYPE_QUERY_COUNTRY + "=" + COUNTRY;


}
