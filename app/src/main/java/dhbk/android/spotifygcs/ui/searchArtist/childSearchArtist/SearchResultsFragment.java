package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.app.SearchManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
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
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.bumptech.glide.load.resource.gif.GifDrawable;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.BindInt;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.MVPApp;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.module.ArtistSearchModule;
import dhbk.android.spotifygcs.ui.recyclerview.ArtistItemListener;
import dhbk.android.spotifygcs.ui.recyclerview.SlideInItemAnimator;
import dhbk.android.spotifygcs.ui.showTopTracksArtist.ShowTopTracksActivity;
import dhbk.android.spotifygcs.ui.widget.BaselineGridTextView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.ImeUtils;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

// TODO: 7/19/2016 implement onNewIntent()
public class SearchResultsFragment extends BaseFragment implements
        SearchChildContract.View,
        ArtistItemListener {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";

    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
    private static final int REQUEST_CODE_VIEW_SHOT = 5407;

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

    //     get adapter components
    @Inject
    SearchResultsAdapter mSearchResultsAdapter;
    @Inject
    ArtistSearchInteractor mArtistSearchInteractor;
    @BindView(R.id.results_scrim)
    View resultsScrim;

    private boolean dismissing = false;
    // location of the search icon
    private int searchBackDistanceX;
    private int searchIconCenterX;
    private SearchChildContract.Presenter mPresenter;
    private Transition mAutoTransition;
    private BaselineGridTextView noResults;
    private Transition auto;
    public static Drawable sDrawable;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchBackDistanceX = getArguments().getInt(ARG_SEARCH_BACK_DISTANCE_X);
            searchIconCenterX = getArguments().getInt(ARG_SEARCH_ICON_CENTER_X);
        }
    }

    @Override
    public int getLayout() {
        return R.layout.fragment_test_translucent;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {
        // inject ArtistSearchComponent in this view
        MVPApp.getApp(getContext())
                .getSpotifyStreamerComponent()
                .artistSearchComponent(new ArtistSearchModule(this))
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    protected void initView() {
        startTransition();
    }

    @Override
    public void animSearchView() {
        // anim search bar
        // extract the search icon's location passed from the launching activity, minus 4dp to
        // compensate for different paddings in the views
        searchBackDistanceX = getActivity().getIntent().getIntExtra(EXTRA_MENU_LEFT, 0) - (int) TypedValue
                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());
        searchIconCenterX = getActivity().getIntent().getIntExtra(EXTRA_MENU_CENTER_X, 0);

        // translate icon to match the launching screen then animate back into position
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
        searchBack.postDelayed(new Runnable() {
            @Override
            public void run() {
                searchBack.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_arrow_back_padded));
            }
        }, 600L);

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
                                (float) Math.hypot(searchBackDistanceX, scrim.getHeight()
                                        - searchBackground.getBottom())),
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
        if (dismissing) return;
        dismissing = true;

        // translate the icon to match position in the launching activity
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
    }

    @Override
    public boolean isActive() {
        return isAdded();
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
//        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
//            if (hasFocus && confirmSaveContainer.getVisibility() == View.VISIBLE) {
//                hideSaveConfimation();
//            }
//        });
    }

    @Override
    public ArtistSearchInteractor getArtistSearchInteractor() {
        checkNotNull(mArtistSearchInteractor, "ArtistSearchInteractor cannot be null");
        return mArtistSearchInteractor;
    }

    // callback when query the spotify api, if found the artists
    @Override
    public void displaySearchArtists(ArrayList<Artist> artists) {
        // if has results, show it to recycler view
        if (artists != null && artists.size() > 0) {
            mPresenter.doOtherThingToShowResults();
            // change data to adapter
            mSearchResultsAdapter.replaceAnotherData(artists);
        } else {
            // if not have result, info to user
            mPresenter.infoUsersNotHaveData();
        }
    }

    // delay transition
    // remove the progress bar
    // set the rcv to visible
    @Override
    public void showtoRcv() {
        endTransition();
        progress.setVisibility(View.GONE);
        results.setVisibility(View.VISIBLE);
    }

    // if we dont have data to show, info the user
    @Override
    public void showEmptyArtistsLayout() {
        endTransition();
        progress.setVisibility(View.GONE);
        setNoResultsVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(SearchChildContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    // when app is on pause state, do something to release resources.
    @Override
    protected void doThingWhenPauseApp() {
        int stopAnimation = 0;
        getActivity().overridePendingTransition(stopAnimation, stopAnimation);
    }

    // when app is on destroy state, stop network.
    @Override
    protected void doThingWhenDestroyApp() {
        sDrawable = null;
        // TODO: 7/19/2016 stop loading network anymore.
    }


    private void startTransition() {
        auto = TransitionInflater.from(getContext()).inflateTransition(R.transition.auto);
    }

    private void endTransition() {
        TransitionManager.beginDelayedTransition(container, mAutoTransition);
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
        TransitionManager.beginDelayedTransition(container, mAutoTransition);
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
        });
    }

    // called when click an artist in this view
    // go to show detail top tracks of artist
    @Override
    public void onArtistClick(Artist artist, View image) {
        // anim when open second activity
//        setGridItemContentTransitions(image);
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                getActivity(),
                Pair.create(image, getActivity().getString(R.string.transition_shot)),
                Pair.create(image, getActivity().getString(R.string.transition_shot_background)));

        sDrawable = ((ImageView)image).getDrawable();

        // pass id of a artist to second activity
        startActivityForResult(ShowTopTracksActivity.createStartIntent(getContext(), artist.getIdArtist(), artist.getNameArtist()), REQUEST_CODE_VIEW_SHOT, options.toBundle());
//        startActivity(ShowTopTracksActivity.createStartIntent(getContext(), artist.getIdArtist(), artist.getUrlLargeImage()));
    }

    @Override
    public boolean onArtistTouch(View view, MotionEvent event) {
        // check if it's an event we care about, else bail fast
        final int action = event.getAction();
        if (!(action == MotionEvent.ACTION_DOWN
                || action == MotionEvent.ACTION_UP
                || action == MotionEvent.ACTION_CANCEL)) return false;

        // get the image and check if it's an animated GIF
        final Drawable drawable = ((ImageView)view).getDrawable();
        if (drawable == null) return false;
        GifDrawable gif = null;
        if (drawable instanceof GifDrawable) {
            gif = (GifDrawable) drawable;
        } else if (drawable instanceof TransitionDrawable) {
            // we fade in images on load which uses a TransitionDrawable; check its layers
            TransitionDrawable fadingIn = (TransitionDrawable) drawable;
            for (int i = 0; i < fadingIn.getNumberOfLayers(); i++) {
                if (fadingIn.getDrawable(i) instanceof GifDrawable) {
                    gif = (GifDrawable) fadingIn.getDrawable(i);
                    break;
                }
            }
        }
        if (gif == null) return false;
        // GIF found, start/stop it on press/lift
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                gif.start();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                gif.stop();
                break;
        }
        return false;
    }


    /**
     * The shared element transition to dribbble shots & dn stories can intersect with the FAB.
     * This can cause a strange layers-passing-through-each-other effect. On return hide the FAB
     * and animate it back in after the transition.
     */
//    private void setGridItemContentTransitions(View gridItem) {
////        final View fab = getActivity().findViewById(R.id.fab);
////        if (!ViewUtils.viewsIntersect(gridItem, fab)) return;
////
////        final Transition reenter = TransitionInflater.from(getActivity())
////                .inflateTransition(R.transition.home_content_item_reenter);
//        // we only want these content transitions in certain cases so clear out when done.
//        reenter.addListener(new AnimUtils.TransitionListenerAdapter() {
//            @Override
//            public void onTransitionStart(Transition transition) {
////                fab.setAlpha(0f);
////                fab.setScaleX(0f);
////                fab.setScaleY(0f);
//            }
//
//            @Override
//            public void onTransitionEnd(Transition transition) {
//                getActivity().getWindow().setReenterTransition(null);
//            }
//        });
//        getActivity().getWindow().setReenterTransition(reenter);
//    }

}
