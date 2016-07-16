package dhbk.android.spotifygcs.component;

import dagger.Component;
import dhbk.android.spotifygcs.ActivityScope;
import dhbk.android.spotifygcs.module.ArtistSearchModule;
import dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist.SearchChildFragment;

/**
 * Created by huynhducthanhphong on 7/16/16.
 * this is a subdependence, define something to search when connect to spotify api.
 */
@Component(dependencies = SpotifyStreamerComponent.class, modules = ArtistSearchModule.class)
@ActivityScope
public interface ArtistSearchComponent {
    void inject(SearchChildFragment searchFragment);
//    SearchResultsAdapter getAdapter();
}
