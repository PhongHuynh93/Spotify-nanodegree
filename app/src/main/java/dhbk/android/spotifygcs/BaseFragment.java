package dhbk.android.spotifygcs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import dhbk.android.spotifygcs.component.SpotifyStreamerComponent;

/**
 * Created by huynhducthanhphong on 7/16/16.
 */
public abstract class BaseFragment extends Fragment {

    // inject view and dependance
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(getLayout(), container, false);
        injectViews(v);
        injectDependencies();
        if (hasToolbar()) {
            setHasOptionsMenu(true);
        }
        initView();
        return v;
    }

    // when view start, start the presenter too
    @Override
    public void onStart() {
        super.onStart();
        getPresenter().start();
    }

    @Override
    public void onPause() {
        doThingWhenPauseApp();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        doThingWhenDestroyApp();
        super.onDestroy();
    }

    // when app is on pause state, do something to release resources.
    protected abstract void doThingWhenPauseApp();

    // when app is on destroy state, stop network.
    protected abstract void doThingWhenDestroyApp();

    // return layout for fragment
    public abstract int getLayout();

    // if view use any dagger, call component
    public abstract void setUpComponent(SpotifyStreamerComponent appComponent);


    /**
     * @return The presenter attached to the fragment. This must extends from {@link BasePresenter}
     * */
    protected abstract BasePresenter getPresenter();

    // check a view has toolbar or not
    protected abstract boolean hasToolbar();

    // init view object in view
    protected abstract void initView();

    /**
     * Setup the object graph and inject the dependencies needed on this fragment.
     */
    private void injectDependencies() {
        setUpComponent(MVPApp.getApp(getContext()).getSpotifyStreamerComponent());
    }


    // Every object annotated with {@link butterknife.Bind} its gonna injected trough butterknife
    private void injectViews(View v) {
        ButterKnife.bind(this, v);
    }


}
