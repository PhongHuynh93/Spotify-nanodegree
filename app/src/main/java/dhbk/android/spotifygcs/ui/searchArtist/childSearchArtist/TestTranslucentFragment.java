package dhbk.android.spotifygcs.ui.searchArtist.childSearchArtist;

import android.os.Bundle;

import java.util.ArrayList;

import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.domain.Artist;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestTranslucentFragment extends BaseFragment implements SearchChildContract.View {
    private static final String ARG_SEARCH_BACK_DISTANCE_X = "searchBackDistanceX";
    private static final String ARG_SEARCH_ICON_CENTER_X = "searchIconCenterX";
    private static final int NUMBER_OF_COLUMN_LIST = 2;
    private SearchChildContract.Presenter mPresenter;

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

    @Override
    public void dismiss() {

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
}
