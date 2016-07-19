package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.transition.Transition;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
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

import static com.google.common.base.Preconditions.checkNotNull;

public class TestTranslucentFragment extends BaseFragment implements SearchChildContract.View {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";
    private static final int NUMBER_OF_COLUMN_LIST = 2;
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
    @BindView(android.R.id.empty)
    ProgressBar mEmpty;
    @BindView(R.id.stub_no_search_results)
    ViewStub mStubNoSearchResults;
    @BindView(R.id.search_results)
    RecyclerView mSearchResults;
    @BindView(R.id.results_scrim)
    View mResultsScrim;
    @BindView(R.id.results_container)
    FrameLayout mResultsContainer;
    @BindView(R.id.container)
    FrameLayout mContainer;

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


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_test_translucent, container, false);
//    }

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
