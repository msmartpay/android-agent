package in.msmartpay.agent;

import android.animation.ObjectAnimator;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.Keys;
import in.msmartpay.agent.utility.L;
import in.msmartpay.agent.utility.Util;

public class SplashActivity extends BaseActivity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private ProgressBar mprogressBar;
    private ImageView imageView;
    private static final int requestLocation = 0;
    private String longitude = "", latitude = "";


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
        Util.SavePrefData(SplashActivity.this, getString(R.string.latitude), latitude);
        Util.SavePrefData(SplashActivity.this, getString(R.string.longitude), longitude);


        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(2000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();
        startNextActivity();


    }

    private void startNextActivity() {
        // if(latitude!=null && longitude!=null)
        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */
        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            // Start your app main activity

            String loginDetails = readSourceFile(getApplicationContext(), Keys.MY_FILE);
            if (loginDetails != null && !loginDetails.equalsIgnoreCase("")) {
                L.m2("check--1>", "Login1");
                String[] cred = loginDetails.split("&");
                if (cred.length >= 2) {
                    String user = cred[0];
                    String pass = cred[1];
                    String response = Util.LoadPrefData(getApplicationContext(), getString(R.string.user_credential));
                    startLoginActivity();
                } else {
                    createSourceFile(getApplicationContext(), "", Keys.MY_FILE);
                    startLoginActivity();
                }
            } else {
                startLoginActivity();
            }

        }, 2000);
    }


}