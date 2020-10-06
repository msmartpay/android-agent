package msmartpay.in;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;

import msmartpay.in.utility.BaseActivity;
import msmartpay.in.utility.L;

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

        Animation anim1 = AnimationUtils.loadAnimation(this,R.anim.translate);
        imageView =(ImageView)findViewById(R.id.imageView);
        imageView.setAnimation(anim1);
        mprogressBar = (ProgressBar) findViewById(R.id.progressBar);
        ObjectAnimator anim = ObjectAnimator.ofInt(mprogressBar, "progress", 0, 100);
        anim.setDuration(4000);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        enableBackgroundService();

    }
    @SuppressLint("BatteryLife")
    private void enableBackgroundService(){
        pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isIgnoringBatteryOptimizations = false;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());

            if (!isIgnoringBatteryOptimizations) {

                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, MY_IGNORE_OPTIMIZATION_REQUEST);

            }else {
                startNextActivity();
            }
        }else {
            startNextActivity();
        }

    }
    private void startNextActivity(){
        Handler handler = new Handler();

        handler.postDelayed(() -> {
            startActivity(new Intent(SplashScreenActivity.this,LoginActivity.class));
            finish();

        },3000);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_IGNORE_OPTIMIZATION_REQUEST) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                boolean isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(getPackageName());
                if (isIgnoringBatteryOptimizations) {
                    // Ignoring battery optimization
                   startNextActivity();
                   L.s(getApplicationContext(),"Ignoring battery optimization");
                } else {
                    enableBackgroundService();
                    L.s(getApplicationContext(),"Please allow this option...");
                    // Not ignoring battery optimization
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}