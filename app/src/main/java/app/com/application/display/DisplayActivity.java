package app.com.application.display;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import app.com.application.R;
import app.com.application.main.DataModel;
import app.com.application.utils.NetworkUtils;
import butterknife.Bind;
import butterknife.ButterKnife;

import static app.com.application.AppConstants.EXTRA_DATA_MODEL;

public class DisplayActivity extends AppCompatActivity {


    @Bind(R.id.wv_data)
    WebView wvData;
    @Bind(R.id.tv_status)
    TextView tvStatus;

    private DataModel dataModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dataModel = getIntent().getParcelableExtra(EXTRA_DATA_MODEL);
        if (null != dataModel) {

            wvData.setWebViewClient(new LoadWebViewClient());

//setting up local cache for offline loading of content
            wvData.getSettings().setAppCacheMaxSize(5 * 1024 * 1024); // 5MB
            wvData.getSettings().setAppCachePath(getApplicationContext().getCacheDir().getAbsolutePath());
            wvData.getSettings().setAllowFileAccess(true);
            wvData.getSettings().setAppCacheEnabled(true);
            wvData.getSettings().setJavaScriptEnabled(true);
            wvData.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // load online by default

            if (!NetworkUtils.isNetworkAvailable(this)) { // loading offline
                wvData.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            }
            //Load url in webview
            wvData.loadUrl(dataModel.getURL());
            getSupportActionBar().setTitle(dataModel.getPublisher());


        }

    }

    private class LoadWebViewClient extends WebViewClient {


        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);

        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            wvData.setVisibility(View.GONE);
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText(R.string.info_web_page_failed);
        }


    }


    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {
        if (wvData.canGoBack()) {
            wvData.goBack();
        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }
}


