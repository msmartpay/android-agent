package in.msmartpay.agent.helpAndSupport;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import in.msmartpay.agent.MainActivity;
import in.msmartpay.agent.R;
import in.msmartpay.agent.utility.BaseActivity;
import in.msmartpay.agent.utility.ProgressBarAnimation;

import java.util.Stack;

/**
 * Created by Harendra on 5/6/2017.
 */

public class AboutUsWebViewActivity extends BaseActivity {

    private WebView webView;
    private String lasturl;
    private Stack<String> urls = new Stack<String>();
    private ProgressBar progressBar;
    private ProgressBarAnimation anim;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us_webview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("About Us");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        anim = new ProgressBarAnimation(progressBar,1000);
        webView = (WebView) findViewById(R.id.wv_wallet);
        Intent intent = getIntent();
        lasturl = intent.getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.setWebChromeClient(new WebChromeClientDemo());
        webView.loadUrl(lasturl);


        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                anim.setProgress(0);
                System.out.println("loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                anim.setProgress(100);
                System.out.println("loading.. finish" + url);
                if(url.equals("http://smartkinda.com/AGG/PGRequest.jsp")){
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }
    public boolean onKeyDown(int keyCode, KeyEvent evt) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (urls.size() > 0) {
                lasturl = urls.pop();
                webView.loadUrl(lasturl);
            } else
                finish();
            return true;
        }

        return false;
    }
    private class WebChromeClientDemo extends WebChromeClient {
        public void onProgressChanged(WebView view, int progress) {
            progressBar.setProgress(progress);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
