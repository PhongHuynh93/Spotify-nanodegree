package dhbk.android.spotifygcs;

import android.app.Application;
import android.content.Context;

import dhbk.android.spotifygcs.component.DaggerSpotifyStreamerComponent;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.module.SpotifyStreamerModule;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by phongdth.ky on 7/13/2016.
 */
public class MVPApp extends Application {
    private SpotifyStreamerComponent mSpotifyStreamerComponent;

    // get this application
    public static MVPApp getApp(Context context) {
        return (MVPApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setupFont();
        setupGraph();
    }

    // contains dependency use to inject into class
    private void setupGraph() {
        // we use subcomponent, this is a parent dependance
        mSpotifyStreamerComponent = DaggerSpotifyStreamerComponent
                .builder()
                .spotifyStreamerModule(new SpotifyStreamerModule(this))
                .build();
    }

    // todo setup custome font for all my views
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
