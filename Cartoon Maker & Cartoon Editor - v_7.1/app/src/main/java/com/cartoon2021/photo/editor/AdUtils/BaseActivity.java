package com.cartoon2021.photo.editor.AdUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cartoon2021.photo.editor.R;

public class BaseActivity extends AppCompatActivity {

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this, R.style.MyAlertDialogStyle);
        progressDialog.setMessage("Loading Ads...");
        progressDialog.setCancelable(false);
    }

    public void showInterstitial(Activity activity) {
//        if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//            AdUtils.showInterstitial(activity, progressDialog);
//            return;
//        }
        AdUtils.showInterstitial(activity, progressDialog);
    }

    public void showBannerAd(Activity activity, RelativeLayout relativeLayout) {
//        if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//            AdUtils.showBannerAds(activity, relativeLayout);
//        }
        AdUtils.showBannerAds(activity, relativeLayout);
    }

    public void showNativeAd(Activity activity, RelativeLayout relativeLayout) {
//        if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//            AdUtils.showNativeAds(activity, relativeLayout);
//        }
        AdUtils.showNativeAds(activity, relativeLayout);
    }

    public void showMiniNativeAd(Activity activity, RelativeLayout relativeLayout) {
//        if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
//            AdUtils.showMiniNativeAds(activity, relativeLayout);
//        }
        AdUtils.showMiniNativeAds(activity, relativeLayout);
    }

//    public void showRewardedInterstitialAd(VideoMakingActivity activity, AdUtils.RewardedDismissed rewardedDismissed) {
//        AdUtils.showRewardedAd(activity, rewardedDismissed, progressDialog);
//    }

    public void showGoogleNativeInRecyclerView(Activity activity, RelativeLayout relativeLayout) {
        if (AppUtility.getBoolean(activity, Constant.IS_GOOGLE_AD, false)) {
            AdUtils.setUpGoogleNativeInRecyclerView(activity, relativeLayout);
        }
    }
}