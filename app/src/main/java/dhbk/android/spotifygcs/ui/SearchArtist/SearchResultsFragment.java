package dhbk.android.spotifygcs.ui.SearchArtist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.SpotifyInteractor;
import dhbk.android.spotifygcs.module.ArtistSearchModule;
import dhbk.android.spotifygcs.ui.recyclerview.ArtistItemListener;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.widget.BaselineGridTextView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.ImeUtils;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

// TODO: 7/19/2016 implement onNewIntent()
public class SearchResultsFragment extends BaseFragment implements
        SearchResultsContract.View,
        ArtistItemListener {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";
    public static Drawable sDrawable;

    @Inject
    SearchResultsAdapter mSearchResultsAdapter;
    @Inject
    SpotifyInteractor mSpotifyInteractor;

    @BindView(R.id.searchback)
    ImageButton searchBack;
    @BindView(R.id.searchback_container)
    ViewGroup searchBackContainer;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.search_background)
    View searchBackground;
    @BindView(android.R.id.empty)
    ProgressBar progress;
    @BindView(R.id.container)
    ViewGroup container;
    @BindView(R.id.search_toolbar)
    ViewGroup searchToolbar;
    @BindView(R.id.scrim)
    View scrim;
    @BindView(R.id.results_container)
    ViewGroup resultsContainer;
    @BindView(R.id.search_results)
    RecyclerView results;
    @BindInt(R.integer.num_col)
    int NUMBER_OF_COLUMN_LIST;
    @BindView(R.id.results_scrim)
    View resultsScrim;
    private int searchBackDistanceX;
    private int searchIconCenterX;
    private SearchResultsContract.Presenter mPresenter;
    private BaselineGridTextView noResults;
    private Transition auto;
    private boolean dismissing = false;

    public SearchResultsFragment() {
        // Required empty public constructor
    }

    // get the location of search icon to make an anim
    public static SearchResultsFragment newInstance(int searchBackDistanceX, int searchIconCenterX) {
        SearchResultsFragment searchChildFragment = new SearchResultsFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARG_SEARCH_BACK_DISTANCE_X, searchBackDistanceX);
        arg.putInt(ARG_SEARCH_ICON_CENTER_X, searchIconCenterX);
        searchChildFragment.setArguments(arg);
        return searchChildFragment;
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_test_translucent;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent spotifyStreamerComponent) {
        spotifyStreamerComponent.artistSearchComponent(new ArtistSearchModule(this)).inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void setPresenter(SearchResultsContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            searchBackDistanceX = getArguments().getInt(ARG_SEARCH_BACK_DISTANCE_X);
            searchIconCenterX = getArguments().getInt(ARG_SEARCH_ICON_CENTER_X);
        }
        startTransition();
    }

    @Override
    public void animSearchView() {
        // translate icon to match the launching screen then animate back into position
        // move icon search from right to left
        searchBackContainer.setTranslationX(searchBackDistanceX);
        searchBackContainer.animate()
                .translationX(0f)
                .setDuration(650L)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()));

        // transform from search icon to back icon
        AnimatedVectorDrawable searchToBack = (AnimatedVectorDrawable) ContextCompat
                .getDrawable(getContext(), R.drawable.avd_search_to_back);
        searchBack.setImageDrawable(searchToBack);
        searchToBack.start();

        // for some reason the animation doesn't always finish (leaving a part arrow!?) so after
        // the animation set a static drawable. Also animation callbacks weren't added until API23
        // so using post delayed :(
        searchBack.postDelayed(() -> searchBack.setImageDrawable(ContextCompat.getDrawable(getContext(),
                R.drawable.ic_arrow_back_padded)), 600L);

        // fade in the other search chrome
        searchBackground.animate()
                .alpha(1f)
                .setDuration(300L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()));

        searchView.animate()
                .alpha(1f)
                .setStartDelay(400L)
                .setDuration(400L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        searchView.requestFocus();
                        ImeUtils.showIme(searchView);
                    }
                });

        // animate in a scrim over the content behind
        scrim.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                scrim.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimatorSet showScrim = new AnimatorSet();
                showScrim.playTogether(
                        ViewAnimationUtils.createCircularReveal(
                                scrim,
                                searchIconCenterX,
                                searchBackground.getBottom(),
                                0,
                                (float) Math.hypot(searchBackDistanceX, scrim.getHeight() - searchBackground.getBottom())),
                        ObjectAnimator.ofArgb(
                                scrim,
                                ViewUtils.BACKGROUND_COLOR,
                                Color.TRANSPARENT,
                                ContextCompat.getColor(getContext(), R.color.grey_opa)));
                showScrim.setDuration(400L);
                showScrim.setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()));
                showScrim.start();
                return false;
            }
        });
    }

    // anim the search icon when close this activity
    @OnClick({R.id.scrim, R.id.searchback})
    @Override
    public void dismiss() {
        // run this code only one time to remove activity, if run multiple time, app will crash due to getActivity() return null.
        if (dismissing) return;
        dismissing = true;

        // translate the icon to match position in the launching activity
        // move from right to left
        new Handler(Looper.getMainLooper()).post(() -> {
            searchBackContainer.animate()
                    .translationX(searchBackDistanceX)
                    .setDuration(600L)
                    .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            getActivity().finishAfterTransition();
                        }
                    })
                    .start();

            // transform from back icon to search icon
            AnimatedVectorDrawable backToSearch = (AnimatedVectorDrawable) ContextCompat
                    .getDrawable(getContext(), R.drawable.avd_back_to_search);
            searchBack.setImageDrawable(backToSearch);

            // clear the background else the touch ripple moves with the translation which looks bad
            searchBack.setBackground(null);
            backToSearch.start();

            // fade out the other search chrome
            searchView.animate()
                    .alpha(0f)
                    .setStartDelay(0L)
                    .setDuration(120L)
                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            // prevent clicks while other anims are finishing
                            searchView.setVisibility(View.INVISIBLE);
                        }
                    })
                    .start();

            searchBackground.animate()
                    .alpha(0f)
                    .setStartDelay(300L)
                    .setDuration(160L)
                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                    .setListener(null)
                    .start();

            if (searchToolbar.getZ() != 0f) {
                searchToolbar.animate()
                        .z(0f)
                        .setDuration(600L)
                        .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                        .start();
            }

            // if we're showing search results, circular hide them
            if (resultsContainer.getHeight() > 0) {
                Animator closeResults = ViewAnimationUtils.createCircularReveal(
                        resultsContainer,
                        searchIconCenterX,
                        0,
                        (float) Math.hypot(searchIconCenterX, resultsContainer.getHeight()),
                        0f);
                closeResults.setDuration(500L);
                closeResults.setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()));
                closeResults.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        resultsContainer.setVisibility(View.INVISIBLE);
                    }
                });
                closeResults.start();
            }

            // fade out the scrim
            scrim.animate()
                    .alpha(0f)
                    .setDuration(400L)
                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                    .setListener(null)
                    .start();
        });

    }


    @Override
    public void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMN_LIST, LinearLayoutManager.VERTICAL, false);
        results.setLayoutManager(gridLayoutManager);
        results.setHasFixedSize(true);
    }

    // set up adapter and animation when load items
    @Override
    public void setupAdapter() {
        checkNotNull(mSearchResultsAdapter, "adapter not be null before set to list");
        results.setAdapter(mSearchResultsAdapter);
        setClickListener();
        results.setItemAnimator(new SlideInItemAnimator());
    }

    @Override
    public void setClickListener() {
        mSearchResultsAdapter.setClickListenerInterface(this);
    }

    @Override
    public void setupSearchBar() {
        SearchManager searchManager = (SearchManager) getContext().getSystemService(getContext().SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        // hint, inputType & ime options seem to be ignored from XML! Set in code
        searchView.setQueryHint(getString(R.string.search_artist_child_text));
        searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        searchView.setImeOptions(searchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (TextUtils.isEmpty(query)) {
                    clearResults();
                }
                return true;
            }
        });
    }

    public SpotifyInteractor getSpotifyInteractor() {
        checkNotNull(mSpotifyInteractor, "SpotifyInteractor cannot be null");
        return mSpotifyInteractor;
    }

    // callback when query the spotify api, if found the artists
    @Override
    public void displaySearchArtists(ArrayList<Artist> artists) {
        // if has results, show it to recycler view
        if (artists != null && artists.size() > 0) {
            if (results.getVisibility() != View.VISIBLE) {
                endTransition();
                progress.setVisibility(View.GONE);
                results.setVisibility(View.VISIBLE);
            }
            // change data to adapter
            mSearchResultsAdapter.replaceAnotherData(artists);
        } else {
            // if not have result, info to user
            mPresenter.infoUsersNotHaveData();
        }
    }

    // if we dont have data to show, info the user
    @Override
    public void showEmptyArtistsLayout() {
        endTransition();
        progress.setVisibility(View.GONE);
        setNoResultsVisibility(View.VISIBLE);
    }

    @Override
    protected void doThingWhenResumeApp() {

    }

    @Override
    protected void doThingWhenPauseApp() {
        int stopAnimation = 0;
        getActivity().overridePendingTransition(stopAnimation, stopAnimation);
    }

    @Override
    protected void doThingWhenDestroyApp() {
        sDrawable = null;
        // TODO: 7/19/2016 stop loading network anymore.
    }


    private void startTransition() {
        auto = TransitionInflater.from(getContext()).inflateTransition(R.transition.auto);
    }

    private void endTransition() {
        TransitionManager.beginDelayedTransition(container, auto);
    }

    private void searchFor(String query) {
        // when user wait for connection, so a progress will show, clear the old results
        clearResults();
        progress.setVisibility(View.VISIBLE);
        ImeUtils.hideIme(searchView);
        searchView.clearFocus();
        // connect to network and search
        mPresenter.searchArtists(query);
    }

    private void clearResults() {
        mSearchResultsAdapter.clear();
//        dataManager.clear();
        TransitionManager.beginDelayedTransition(container, auto);
        results.setVisibility(View.GONE);
        progress.setVisibility(View.GONE);
//        fab.setVisibility(View.GONE);
//        confirmSaveContainer.setVisibility(View.GONE);
        resultsScrim.setVisibility(View.GONE);
        setNoResultsVisibility(View.GONE);
    }

    // show empty artists layout
    private void setNoResultsVisibility(int visibility) {
        new Handler(Looper.getMainLooper()).post(() -> {
            if (getActivity() != null) {
                if (visibility == View.VISIBLE) {
                    // will be null for the first time
                    if (noResults == null) {
                        // remove text from search view and show keyboard
                        noResults = (BaselineGridTextView) ((ViewStub)
                                getActivity().findViewById(R.id.stub_no_search_results)).inflate();
                        noResults.setOnClickListener(v -> {
                            searchView.setQuery("", false);
                            searchView.requestFocus();
                            ImeUtils.showIme(searchView);
                        });
                    }

                    // show info to user depends on their search
                    String message = String.format(getString(R
                            .string.no_search_results), searchView.getQuery().toString());
                    SpannableStringBuilder ssb = new SpannableStringBuilder(message);
                    ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                            message.indexOf('“') + 1,
                            message.length() - 1,
                            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    noResults.setText(ssb);
                }
                if (noResults != null) {
                    noResults.setVisibility(visibility);
                }
            }

        });
    }

    // go to another activity
    @Override
    public void onArtistClick(Artist artist, View image) {
        sDrawable = ((ImageView) image).getDrawable();
        if (getActivity() != null) {
            ((SearchResultsActivity) getActivity()).goToAnotherActivity(image, artist.getIdArtist(), artist.getNameArtist());
        }
    }

    @Override
    public void setQueryToSearchBar(String query) {
        if (!TextUtils.isEmpty(query)) {
            searchView.setQuery(query, false);
            searchFor(query);
        }
    }
}
