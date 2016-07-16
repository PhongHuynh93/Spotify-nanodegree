package dhbk.android.spotifygcs.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist.SearchChildContract;
import dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist.SearchResultsAdapter;

/**
 * Created by huynhducthanhphong on 7/16/16.
 * // TODO: 7/16/16 command this module
 */
@Module
public class ArtistSearchModule {

    private SearchChildContract.View view;

    public ArtistSearchModule(SearchChildContract.View view) {
        this.view = view;
    }

    // return this view which inject this module
    @Provides
    public SearchChildContract.View provideView() {
        return view;
    }

    // return adapter for this view, context from parent component
    @Provides
    public SearchResultsAdapter provideAdapter(Context context) {
        return new SearchResultsAdapter(context);
    }

//    @Provides
//    public ArtistSearchInteractor provideSearchIntertor(ArtistSearchInteractor artistSearchInteractor) {
//        return artistSearchInteractor;
//    }
}
