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
        return v;
    }

    // when view start, start the presenter too
    @Override
    public void onStart() {
        super.onStart();
        getPresenter().start();
    }

    // return layout for fragment
    public abstract int getLayout();

    // if view use any dagger, call component
    public abstract void setUpComponent(SpotifyStreamerComponent appComponent);


    /**
     * @return The presenter attached to the fragment. This must extends from {@link BasePresenter}
     * */
    protected abstract BasePresenter getPresenter();

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
