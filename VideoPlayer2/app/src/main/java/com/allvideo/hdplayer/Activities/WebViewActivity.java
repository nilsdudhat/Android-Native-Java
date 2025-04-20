package com.allvideo.hdplayer.Activities;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Bundle;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.allvideo.hdplayer.AdsIntegration.AdUtils;
import com.allvideo.hdplayer.R;

public class WebViewActivity extends AppCompatActivity {

    WebView web_view;
    ImageView img_back;

    TextView txt_title;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        AdUtils.showInterstitial(WebViewActivity.this);
        AdUtils.showBanner(WebViewActivity.this, findViewById(R.id.ad_banner));

        img_back = findViewById(R.id.img_back);
        web_view = findViewById(R.id.web_view);
        txt_title = findViewById(R.id.txt_title);

        img_back.setOnClickListener(v -> onBackPressed());

        web_view.getSettings().setJavaScriptEnabled(true); // enable javascript

        web_view.setWebViewClient(new WebViewClient() {
            @SuppressWarnings("deprecation")
            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(WebViewActivity.this, description, Toast.LENGTH_SHORT).show();
            }
            @TargetApi(android.os.Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                // Redirect to deprecated method, so you can use it in all SDK versions
                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
            }
        });

//        String str_web_url = getIntent().getStringExtra("web_view");

//        if (str_web_url.equals("quareka")) {
//            web_view.loadUrl("https://317.win.qureka.com/");
//            txt_title.setText("Play Games");
//        } else if (str_web_url.equals("privacy")) {
            web_view.loadUrl("https://allvideo-hd-video.flycricket.io/privacy.html");
            txt_title.setText("Privacy Policy");
//        }

    }
}
