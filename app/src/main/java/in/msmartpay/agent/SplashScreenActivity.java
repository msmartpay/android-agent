package in.msmartpay.agent;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import in.msmartpay.agent.location.test.LocationTestActivity;
import in.msmartpay.agent.utility.BaseActivity;

public class SplashScreenActivity extends BaseActivity {

    public static final int MY_IGNORE_OPTIMIZATION_REQUEST = 111;
    PowerManager pm;
    private ProgressBar mprogressBar;
    private ImageView imageView;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        getSupportActionBar().hide();

        Animation anim1 = AnimationUtils.loadAnimation(this, R.anim.translate);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setAnimation(anim1);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(4000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        startNextActivity();

    }

    private void startNextActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            //startActivity(new Intent(SplashScreenActivity.this, LocationTestActivity.class));
            finish();

        }, 3000);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}