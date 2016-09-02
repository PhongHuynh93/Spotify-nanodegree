package dhbk.android.spotifygcs.ui.splash;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.ui.showYourReposition.SearchArtistActiviy;
import dhbk.android.spotifygcs.util.HelpUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * this activity coontains a login screen, so user can log in by their spotify account
 */
public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.imageview_splash_headphone)
    ImageView mImageviewSplashHeadphone;
    @BindView(R.id.textview_splash_logan)
    TextView mTextviewSplashLogan;
    @BindView(R.id.button_splash_go_to_search_activity)
    Button mButtonSplashGoToSearchActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //todo  because before nav to this activity, the app load theme for brand
        // so after loading brand done, nav to Splash Theme
        setTheme(R.style.AppThemeNoActionBarSplash);
        //todo  make this activity go full screen - hide navigation bottom bar and status bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
        animate();
    }

    // use this method for an activity to change font of text
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // anim view on the screen, amin with different start delay
    private void animate() {
        // anim image - move upward
        Animator setImage = AnimatorInflater.loadAnimator(this, R.animator.main_move_translation_y_headphone);
        setImage.setTarget(mImageviewSplashHeadphone);
        setImage.start();

        // anim text - move downward and appear
        Animator setText = AnimatorInflater.loadAnimator(this, R.animator.main_move_translation_y_text);
        setText.setTarget(mTextviewSplashLogan);
        setText.start();

        // anim button - zoom out
        Animator setButton = AnimatorInflater.loadAnimator(this, R.animator.main_scale_button);
        setButton.setTarget(mButtonSplashGoToSearchActivity);
        setButton.start();
    }

    private void initView() {
        mTextviewSplashLogan.setText(HelpUtil.getSpannedText(this, R.string.splash_text));
    }

    // when click button, go to search artist activity.
    @OnClick(R.id.button_splash_go_to_search_activity)
    public void onClick() {
        startActivity(SearchArtistActiviy.createStartIntent(this));
    }
}
