package dhbk.android.spotifygcs.io;

/**
 * Created by phongdth.ky on 7/15/2016.
 * Constants used for requests to API
 * Web API Endpoint Reference, watch this link below to see full of endpoint that web api returns.
 * https://developer.spotify.com/web-api/endpoint-reference/
 */
public class SpotifyApiConstants {
    // base url for spotify api
    public static final String BASE_URL = "https://api.spotify.com";

    // a path to get artists from api
    private static final String VERSION_PATH = "/v1";
    private static final String SEARCH_PATH = "/search";
    private static final String TYPE_QUERY = "type";
    public static final String QUERY_TO_SEARCH = "q";
    private static final String ARTIST = "artist";
    // depend on link above, the way to search the artists is GET /v1/search?type=artist
    public static final String ARTIST_SEARCH_URL = VERSION_PATH + SEARCH_PATH + "?"+ TYPE_QUERY + "=" + ARTIST;


    // a path to get a top track from an artist
//    GET https://api.spotify.com/v1/artists/{id}/top-tracks
    private static final String SEARCH_TOP_TRACK_PATH = "/artists";
    public static final String ID_ARTIST = "id";
    private static final String TOP_TRACK = "/top-tracks";
    private static final String TYPE_QUERY_COUNTRY = "country";
    private static final String COUNTRY = "US";

    public static final String TOP_TRACK_SEARCH_URL = VERSION_PATH + SEARCH_TOP_TRACK_PATH + "/{" + ID_ARTIST + "}" + TOP_TRACK
            + TYPE_QUERY_COUNTRY + "=" + COUNTRY;


}
