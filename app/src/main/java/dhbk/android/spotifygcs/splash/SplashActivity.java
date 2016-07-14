package dhbk.android.spotifygcs.splash;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.R;
import dhbk.android.spotifygcs.searchArtist.SearchArtistActiviy;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
a beginner activity: which show a headphone icon + text + button with animation
because it's a simple layout with no background tasks, so no fragment here
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
        setTheme(R.style.AppThemeNoActionBar); // set theme default
        if (Build.VERSION.SDK_INT >= 16) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initView();
    }


    // animation when layout load successful
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        animate();
        super.onWindowFocusChanged(hasFocus);
    }

    // use this method for an activity to change font of text
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    // anim view on the screen, amin with different start delay
    private void animate() {
        // anim image - move upward
        ObjectAnimator setImage = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.main_move_translation_y_headphone);
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

    // load view
    private void initView() {
        Spanned formattedText;
        // check because in android N, Html.fromHtml is deprecated
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            formattedText = Html.fromHtml(getResources().getString(R.string.splash_text), Html.FROM_HTML_MODE_LEGACY);
        } else {
            formattedText = Html.fromHtml(getResources().getString(R.string.splash_text));
        }
        mTextviewSplashLogan.setText(formattedText);

    }

    // when click button, go to search artist activity.
    @OnClick(R.id.button_splash_go_to_search_activity)
    public void onClick() {
        Intent gotoSearchArtistAcIntent = new Intent(this, SearchArtistActiviy.class);
        startActivity(gotoSearchArtistAcIntent);
    }
}
