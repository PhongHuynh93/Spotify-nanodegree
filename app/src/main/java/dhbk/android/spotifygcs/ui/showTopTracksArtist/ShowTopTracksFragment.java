package dhbk.android.spotifygcs.ui.showTopTracksArtist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dhbk.android.spotifygcs.BaseFragment;
import dhbk.android.spotifygcs.BasePresenter;
import dhbk.android.spotifygcs.MVPApp;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;
import dhbk.android.spotifygcs.interactor.ArtistSearchInteractor;
import dhbk.android.spotifygcs.module.TopTrackModule;
import dhbk.android.spotifygcs.util.ViewUtils;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowTopTracksFragment extends BaseFragment implements
        ShowTopTracksContract.View {
    private static final String ARG_ARTIST_ID = "artist_id";
    private static final String ARG_URL_IMAGE = "url_name";
    @BindView(R.id.imageview_show_artist)
    ImageView mImageviewShowArtist;
    @BindView(R.id.recyclerview_show_top_track)
    RecyclerView mRecyclerviewShowTopTrack;
    private String mArtistId;
    private ShowTopTracksContract.Presenter mPresenter;

    @Inject
    ArtistSearchInteractor mArtistSearchInteractor;

    public ShowTopTracksFragment() {
    }

    public static ShowTopTracksFragment newInstance(String artistId, String urlLargeImage) {
        ShowTopTracksFragment showTopTracksFragment = new ShowTopTracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ARTIST_ID, artistId);
        args.putString(ARG_URL_IMAGE, urlLargeImage);
        showTopTracksFragment.setArguments(args);
        return showTopTracksFragment;
    }

    @Override
    protected void doThingWhenPauseApp() {

    }

    @Override
    protected void doThingWhenDestroyApp() {

    }

    @Override
    public int getLayout() {
        return R.layout.fragment_show_top_tracks;
    }

    @Override
    public void setUpComponent(SpotifyStreamerComponent appComponent) {
        // inject component (contain adapter) for this view
        MVPApp.getApp(getContext())
                .getSpotifyStreamerComponent()
                .topTrackComponent(new TopTrackModule(this))
                .inject(this);
    }

    @Override
    protected BasePresenter getPresenter() {
        return mPresenter;
    }

    @Override
    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            mArtistId = getArguments().getString(ARG_ARTIST_ID);
            String urlImage = getArguments().getString(ARG_URL_IMAGE);
            // set image
            ViewUtils.setImagePicasso(getContext(), urlImage, mImageviewShowArtist);
        }


    }

    @Override
    public void setPresenter(ShowTopTracksContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public ArtistSearchInteractor getArtistSearchInteractor() {
        checkNotNull(mArtistSearchInteractor, "ArtistSearchInteractor cannot be null");
        return mArtistSearchInteractor;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
