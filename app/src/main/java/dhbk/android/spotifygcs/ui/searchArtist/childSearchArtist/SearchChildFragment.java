package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.SearchManager;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.SearchView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.DaggerArtistSearchComponent;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.module.ArtistSearchModule;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.ImeUtils;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

// TODO: 7/15/2016 listen on search bar when click
public class SearchChildFragment extends BaseFragment implements SearchChildContract.View {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";
    private static final int NUMBER_OF_COLUMN_LIST = 2;

    // get adapter components
    @Inject
    SearchResultsAdapter mSearchResultsAdapter;

    @BindView(R.id.scrim)
    View mScrim;
    @BindView(R.id.search_background)
    View mSearchBackground;
    @BindView(R.id.search_view)
    SearchView mSearchView;
    @BindView(R.id.searchback)
    ImageButton mSearchback;
    @BindView(R.id.searchback_container)
    FrameLayout mSearchbackContainer;
    @BindView(R.id.search_toolbar)
    FrameLayout mSearchToolbar;
    @BindView(R.id.container)
    FrameLayout mContainer;
    @BindView(R.id.recyclerview_search_artist)
    RecyclerView mRecyclerviewSearchArtist;

    private boolean dismissing = false;
    // location of the search icon
    private int searchBackDistanceX;
    private int searchIconCenterX;
    private SearchChildContract.Presenter mPresenter;

    public SearchChildFragment() {
    }

    public static SearchChildFragment newInstance(int searchBackDistanceX, int searchIconCenterX) {
        SearchChildFragment searchChildFragment = new SearchChildFragment();
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

    ////////////////////////////////////////////////////////////////////////////
    // implement parent class
    @Override
    public int getLayout() {
        return R.layout.fragment_search_child;
    }

    // inject component, by passing parent component
    // DaggerArtistSearchComponent contains adapter for this fragment to use to show a list of artists
    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {
        DaggerArtistSearchComponent.builder()
                .spotifyStreamerComponent(appComponent)
                .artistSearchModule(new ArtistSearchModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }


    ////////////////////////////////////////////////////////////////////////////
    // implement interface
    // anim the search icon when open this activity
    @Override
    public void animSearchView() {
        // translate icon to match the launching screen then animate back into position
        mSearchbackContainer.setTranslationX(searchBackDistanceX);
        mSearchbackContainer.animate()
                .translationX(0f)
                .setDuration(650L)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()));
        // transform from search icon to back icon
        AnimatedVectorDrawable searchToBack = (AnimatedVectorDrawable) ContextCompat
                .getDrawable(getContext(), R.drawable.avd_search_to_back);
        mSearchback.setImageDrawable(searchToBack);
        searchToBack.start();
        // for some reason the animation doesn't always finish (leaving a part arrow!?) so after
        // the animation set a static drawable. Also animation callbacks weren't added until API23
        // so using post delayed :(
        mSearchback.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchback.setImageDrawable(ContextCompat.getDrawable(getContext(),
                        R.drawable.ic_arrow_back_padded));
            }
        }, 600L);

        // fade in the other search chrome
        mSearchBackground.animate()
                .alpha(1f)
                .setDuration(300L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()));
        mSearchView.animate()
                .alpha(1f)
                .setStartDelay(400L)
                .setDuration(400L)
                .setInterpolator(AnimUtils.getLinearOutSlowInInterpolator(getContext()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // when open the search bar, focus on this search bar and show keyboard, so when press back the first time, close the keyboard first
                        mSearchView.requestFocus();
                        ImeUtils.showIme(mSearchView);
                    }
                });

        // animate in a scrim over the content behind
        mScrim.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mScrim.getViewTreeObserver().removeOnPreDrawListener(this);
                AnimatorSet showScrim = new AnimatorSet();
                showScrim.playTogether(
                        ViewAnimationUtils.createCircularReveal(
                                mScrim,
                                searchIconCenterX,
                                mSearchBackground.getBottom(),
                                0,
                                (float) Math.hypot(searchBackDistanceX, mScrim.getHeight()
                                        - mSearchBackground.getBottom())),
                        ObjectAnimator.ofArgb(
                                mScrim,
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
        mSearchbackContainer.animate()
                .translationX(searchBackDistanceX)
                .setDuration(600L)
                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()))
                .setListener(new AnimatorListenerAdapter() {
                    // after anim was called successfully, close the activity
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        getActivity().finishAfterTransition();
                    }
                })
                .start();
        // transform from back icon to search icon
        AnimatedVectorDrawable backToSearch = (AnimatedVectorDrawable) ContextCompat
                .getDrawable(getContext(), R.drawable.avd_back_to_search);
        mSearchback.setImageDrawable(backToSearch);
        // clear the background else the touch ripple moves with the translation which looks bad
        mSearchback.setBackground(null);
        backToSearch.start();
        // fade out the other search chrome
        mSearchView.animate()
                .alpha(0f)
                .setStartDelay(0L)
                .setDuration(120L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // prevent clicks while other anims are finishing
                        mSearchView.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
        mSearchBackground.animate()
                .alpha(0f)
                .setStartDelay(300L)
                .setDuration(160L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                .setListener(null)
                .start();
        if (mSearchToolbar.getZ() != 0f) {
            mSearchToolbar.animate()
                    .z(0f)
                    .setDuration(600L)
                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                    .start();
        }

        // fade out the scrim
        mScrim.animate()
                .alpha(0f)
                .setDuration(400L)
                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
                .setListener(null)
                .start();
    }


    // Return true if the fragment is currently added to its activity. (because async when commit, so we not sure when this frag was addes to activity)
    @Override
    public boolean isActive() {
        return isAdded();
    }

    // setup recyclerview
    @Override
    public void setupRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMN_LIST, LinearLayoutManager.VERTICAL, false);
        mRecyclerviewSearchArtist.setLayoutManager(gridLayoutManager);
    }

    // setup adatper to add to recyclerview
    @Override
    public void setupAdapter() {
        mRecyclerviewSearchArtist.setAdapter(mSearchResultsAdapter);
    }

    // setup searchbar
    @Override
    public void setupSearchBar() {
        SearchManager searchManager = (SearchManager) getContext().getSystemService(getContext().SEARCH_SERVICE);
        mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        // hint, inputType & ime options seem to be ignored from XML! Set in code
        mSearchView.setQueryHint(getString(R.string.search_artist_child_text));
        mSearchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        mSearchView.setImeOptions(mSearchView.getImeOptions() | EditorInfo.IME_ACTION_SEARCH |
                EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_FLAG_NO_FULLSCREEN);
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFor(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (TextUtils.isEmpty(query)) {
                    // when remove everytext from search view, remove results
                    clearResults();
                }
                return true;
            }
        });
    }


    @Override
    public void setPresenter(SearchChildContract.Presenter presenter) {
        mPresenter = checkNotNull(presenter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    // search artists with a query
    private void searchFor(String query) {
        // when user wait for connection, so a progress
//        clearResults();
//        progress.setVisibility(View.VISIBLE);
//        ImeUtils.hideIme(searchView);
//        searchView.clearFocus();

//        dataManager.searchFor(query);
    }

    private void clearResults() {

    }
}