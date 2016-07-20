package dhbk.android.spotifygcs.io;

import dhbk.android.spotifygcs.io.model.ArtistSearchResponse;
import dhbk.android.spotifygcs.io.model.TopTrackSearchResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by phongdth.ky on 7/15/2016.
 * All methods that will make a request to Spotify API
 */
public interface SpotifyApiService {
    // get the artists from api depend on link:  https://developer.spotify.com/web-api/endpoint-reference/
    // require query is the keyword for searching
    @GET(SpotifyApiConstants.ARTIST_SEARCH_URL)
    Observable<ArtistSearchResponse> searchArtist(@Query(SpotifyApiConstants.QUERY_TO_SEARCH) String query);

    //    get a top track of an artist
    @GET(SpotifyApiConstants.TOP_TRACK_SEARCH_URL)
    Observable<TopTrackSearchResponse> searchTopTrack(@Path(SpotifyApiConstants.ID_ARTIST) String idArtist);

}
