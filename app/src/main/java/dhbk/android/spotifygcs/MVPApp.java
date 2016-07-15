package dhbk.android.spotifygcs;

import android.app.Application;

import dhbk.android.spotifygcs.module.SpotifyStreamerModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by phongdth.ky on 7/13/2016.
 */
public class MVPApp extends Application {
    private SpotifyStreamerComponent mSpotifyStreamerComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        setupFont();
        setupGraph();
    }

    // contains dependency use to inject into class
    private void setupGraph() {
        mSpotifyStreamerComponent = DaggerSpotifyStreamerComponent.builder()
                .spotifyStreamerModule(new SpotifyStreamerModule(this))
                .build();
    }

    // setup custome font for all my views
    private void setupFont() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Montserrat-Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public SpotifyStreamerComponent getSpotifyStreamerComponent() {
        return mSpotifyStreamerComponent;
    }
}
