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
    //    get artist depend on keyword

    /**
     * @Query là thêm  tham số vào đường dẫn
     * @param query
     * @return
     */
    @GET(SpotifyRequestConstants.ARTIST_SEARCH_URL)
    Observable<ArtistSearchResponse> searchArtist(@Query(SpotifyRequestConstants.QUERY_TO_SEARCH) String query);

    /**
     * tham số @Path là chỗ cần thay vào đường dẫn có {...}
     * @param idArtist
     * @return
     */
    //    get a top track of an artist depend on artist ID.
    @GET(SpotifyRequestConstants.TOP_TRACK_SEARCH_URL)
    Observable<TopTrackSearchResponse> searchTopTrack(@Path(SpotifyRequestConstants.ID_ARTIST) String idArtist);

}
