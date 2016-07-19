package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.SearchView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.ui.widget.BaselineGridTextView;
import dhbk.android.spotifygcs.util.AnimUtils;
import dhbk.android.spotifygcs.util.ImeUtils;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestTranslucentFragment extends BaseFragment implements SearchChildContract.View {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";

    public static final String EXTRA_MENU_LEFT = "EXTRA_MENU_LEFT";
    public static final String EXTRA_MENU_CENTER_X = "EXTRA_MENU_CENTER_X";
//
//    @BindView(R.id.scrim)
//    View mScrim;
//    @BindView(R.id.search_background)
//    View mSearchBackground;
//    @BindView(R.id.search_view)
//    SearchView mSearchView;
//    @BindView(R.id.searchback)
//    ImageButton mSearchback;
//    @BindView(R.id.searchback_container)
//    FrameLayout mSearchbackContainer;
//    @BindView(R.id.search_toolbar)
//    FrameLayout mSearchToolbar;
//    @BindView(android.R.id.empty)
//    ProgressBar mEmpty;
//    @BindView(R.id.stub_no_search_results)
//    ViewStub mStubNoSearchResults;
//    @BindView(R.id.search_results)
//    RecyclerView mSearchResults;
//    @BindView(R.id.results_scrim)
//    View mResultsScrim;
//    @BindView(R.id.results_container)
//    FrameLayout mResultsContainer;
//    @BindView(R.id.container)
//    FrameLayout mContainer;
//
//
//


    @BindView(R.id.searchback) ImageButton searchBack;
    @BindView(R.id.searchback_container) ViewGroup searchBackContainer;
    @BindView(R.id.search_view) SearchView searchView;
    @BindView(R.id.search_background) View searchBackground;
//    @BindView(android.R.id.empty) ProgressBar progress;
    @BindView(R.id.container) ViewGroup container;
    @BindView(R.id.search_toolbar) ViewGroup searchToolbar;
    @BindView(R.id.scrim) View scrim;


    // get adapter components
//    @Inject
//    SearchResultsAdapter mSearchResultsAdapter;
//
//    @Inject
//    ArtistSearchInteractor mArtistSearchInteractor;

    private boolean dismissing = false;
    // location of the search icon
    private int searchBackDistanceX;
    private int searchIconCenterX;
    private SearchChildContract.Presenter mPresenter;
    private Transition mAutoTransition;
    private BaselineGridTextView noResults;
    private Transition auto;

    public TestTranslucentFragment() {
        // Required empty public constructor
    }

    // get the location of search icon to make an anim
    public static TestTranslucentFragment newInstance(int searchBackDistanceX, int searchIconCenterX) {
        TestTranslucentFragment searchChildFragment = new TestTranslucentFragment();
        Bundle arg = new Bundle();
        arg.putInt(ARG_SEARCH_BACK_DISTANCE_X, searchBackDistanceX);
        arg.putInt(ARG_SEARCH_ICON_CENTER_X, searchIconCenterX);
        searchChildFragment.setArguments(arg);
        return searchChildFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auto = TransitionInflater.from(getContext()).inflateTransition(R.transition.auto);

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_test_translucent;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {

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
        // TODO fix properly!!
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
//
//    // anim the search icon when close this activity
    @OnClick({R.id.scrim, R.id.searchback})
    @Override
    public void dismiss() {
//        if (dismissing) return;
//        dismissing = true;
//
//        // translate the icon to match position in the launching activity
//        mSearchbackContainer.animate()
//                .translationX(searchBackDistanceX)
//                .setDuration(600L)
//                .setInterpolator(AnimUtils.getFastOutSlowInInterpolator(getContext()))
//                .setListener(new AnimatorListenerAdapter() {
//                    // after anim was called successfully, close the activity
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        getActivity().finishAfterTransition();
//                    }
//                })
//                .start();
//        // transform from back icon to search icon
//        AnimatedVectorDrawable backToSearch = (AnimatedVectorDrawable) ContextCompat
//                .getDrawable(getContext(), R.drawable.avd_back_to_search);
//        mSearchback.setImageDrawable(backToSearch);
//        // clear the background else the touch ripple moves with the translation which looks bad
//        mSearchback.setBackground(null);
//        backToSearch.start();
//        // fade out the other search chrome
//        mSearchView.animate()
//                .alpha(0f)
//                .setStartDelay(0L)
//                .setDuration(120L)
//                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        // prevent clicks while other anims are finishing
//                        mSearchView.setVisibility(View.INVISIBLE);
//                    }
//                })
//                .start();
//        mSearchBackground.animate()
//                .alpha(0f)
//                .setStartDelay(300L)
//                .setDuration(160L)
//                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
//                .setListener(null)
//                .start();
//        if (mSearchToolbar.getZ() != 0f) {
//            mSearchToolbar.animate()
//                    .z(0f)
//                    .setDuration(600L)
//                    .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
//                    .start();
//        }
//
//        // fade out the scrim
//        mScrim.animate()
//                .alpha(0f)
//                .setDuration(400L)
//                .setInterpolator(AnimUtils.getFastOutLinearInInterpolator(getContext()))
//                .setListener(null)
//                .start();
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public void setupRecyclerView() {

    }

    @Override
    public void setupAdapter() {

    }

    @Override
    public void setupSearchBar() {

    }

    @Override
    public ArtistSearchInteractor getArtistSearchInteractor() {
        return null;
    }

    @Override
    public void displaySearchArtists(ArrayList<Artist> artists) {

    }

    @Override
    public void showtoRcv() {

    }

    @Override
    public void showEmptyArtistsLayout() {

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
}
