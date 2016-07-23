package dhbk.android.spotifygcs.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dhbk.android.spotifygcs.ActivityScope;
import dhbk.android.spotifygcs.ui.searchArtist.SearchResultsAdapter;
import dhbk.android.spotifygcs.ui.searchArtist.SearchResultsContract;

/**
 * Created by huynhducthanhphong on 7/16/16.
 * provide adapter for view to add data to list
 */
@Module
public class ArtistSearchModule {

    private SearchResultsContract.View view;

    /**
     * @param view {@link dhbk.android.spotifygcs.ui.searchArtist.SearchResultsFragment}
     */
    public ArtistSearchModule(SearchResultsContract.View view) {
        this.view = view;
    }

    // return this view which inject this module
    @Provides
    @ActivityScope
    public SearchResultsContract.View provideView() {
        return view;
    }

    // return adapter for this view, context from parent component
    @Provides
    @ActivityScope
    public SearchResultsAdapter provideAdapter(Context context) {
        return new SearchResultsAdapter(context);
    }
}
