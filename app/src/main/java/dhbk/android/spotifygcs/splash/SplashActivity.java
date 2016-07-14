package dhbk.android.spotifygcs.splash;

import android.animation.AnimatorInflater;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dhbk.android.spotifygcs.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppThemeNoActionBar);
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

    // move the image upward
    private void animate() {
        ObjectAnimator set = (ObjectAnimator) AnimatorInflater.loadAnimator(this, R.animator.main_move_translation_y);
        set.setTarget(mImageviewSplashHeadphone);
        set.start();
    }

    // load view
    private void initView() {
        String formattedText = getResources().getString(R.string.splash_text);
        mTextviewSplashLogan.setText(Html.fromHtml(formattedText));

    }

    // when click button, go to search artist activity.
    @OnClick(R.id.button_splash_go_to_search_activity)
    public void onClick() {

    }
}
