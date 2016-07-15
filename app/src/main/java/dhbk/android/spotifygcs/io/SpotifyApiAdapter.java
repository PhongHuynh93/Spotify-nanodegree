package dhbk.android.spotifygcs.io;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by phongdth.ky on 7/15/2016.
 * This class contains the instance of the rest adapter to be connected with SpotifyApiService
 */
public class SpotifyApiAdapter {

    private static Retrofit sRetrofit;

    public static Retrofit getInstance(){
        // because we want this api use rxjava in callback so we attach the adapter to this retrofit instance.
        if(sRetrofit == null)
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(SpotifyApiConstants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

        return sRetrofit;
    }
}
