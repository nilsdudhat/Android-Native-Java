package com.xbeat.videostatus.statusmaker.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.xbeat.videostatus.statusmaker.AdUtils.AdsSettingModel;
import com.xbeat.videostatus.statusmaker.AdUtils.ApiClient;
import com.xbeat.videostatus.statusmaker.AdUtils.ApiRest;
import com.xbeat.videostatus.statusmaker.AdUtils.AppUtility;
import com.xbeat.videostatus.statusmaker.AdUtils.Constant;
import com.xbeat.videostatus.statusmaker.AdUtils.GlobalSettingModel;
import com.xbeat.videostatus.statusmaker.BuildConfig;
import com.xbeat.videostatus.statusmaker.R;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(1024, 1024);

        getSettings();
    }

    private void launchActivity() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, StartActivity.class));
                finish();
            }
        }, 2500);
    }

    private void getSettings() {
        Retrofit retrofit = ApiClient.getClient();
        ApiRest service = retrofit.create(ApiRest.class);
        Call<AdsSettingModel> call = service.getAdsSettings(getPackageName());
        call.enqueue(new Callback<AdsSettingModel>() {
            @Override
            public void onResponse(@NonNull Call<AdsSettingModel> call, @NonNull Response<AdsSettingModel> response) {
                if (response.isSuccessful()) {
                    AppUtility.saveString(SplashActivity.this, Constant.COUNTER, Objects.requireNonNull(response.body()).getData().getCounter() == null ? "5" : response.body().getData().getCounter());
                    AppUtility.saveString(SplashActivity.this, Constant.NATIVE_COUNTER, response.body().getData().getNativeCounter() == null ? "5" : response.body().getData().getNativeCounter());

                    if (AppUtility.getString(SplashActivity.this, Constant.COUNTER, "").trim().isEmpty())
                        AppUtility.saveString(SplashActivity.this, Constant.COUNTER, "5");

                    if (response.body().getData().getGoogleAd() != null)
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, Integer.parseInt(response.body().getData().getGoogleAd()) == 1);
                    else
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, false);

                    if (response.body().getData().getFacebookAd() != null)
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, Integer.parseInt(response.body().getData().getFacebookAd()) == 1);
                    else
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, false);

                    if (response.body().getData().getAppNext() != null)
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_STARTAPP_AD, Integer.parseInt(response.body().getData().getAppNext()) == 1);
                    else
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_STARTAPP_AD, false);

                    if (response.body().getData().getIsQureka() != null)
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_APP_AD_LINK, Integer.parseInt(response.body().getData().getIsQureka()) == 1);
                    else
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_APP_AD_LINK, false);

                    if (response.body().getData().getQurekaId() != null)
                        AppUtility.saveString(SplashActivity.this, Constant.APP_ADS_LINK, response.body().getData().getQurekaId());
                    else
                        AppUtility.saveString(SplashActivity.this, Constant.APP_ADS_LINK, "https://play.google.com/store/apps/details?id=" + getPackageName());

                    AppUtility.saveString(SplashActivity.this, Constant.FACEBOOK_BANNER, response.body().getData().getFacebookBanner() == null ? "" : response.body().getData().getFacebookBanner());
                    AppUtility.saveString(SplashActivity.this, Constant.FACEBOOK_INT, response.body().getData().getFacebookInt() == null ? "" : response.body().getData().getFacebookInt());
                    AppUtility.saveString(SplashActivity.this, Constant.FACEBOOK_NATIVE, response.body().getData().getFacebookNative() == null ? "" : response.body().getData().getFacebookNative());

                    if (AppUtility.getString(SplashActivity.this, Constant.FACEBOOK_BANNER, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.FACEBOOK_INT, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.FACEBOOK_NATIVE, "").equalsIgnoreCase(""))
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, false);

                    AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_BANNER, response.body().getData().getGoogleBanner() == null ? "" : response.body().getData().getGoogleBanner());
                    AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_INT, response.body().getData().getGoogleInt() == null ? "" : response.body().getData().getGoogleInt());
                    AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_NATIVE, response.body().getData().getGoogleNative() == null ? "" : response.body().getData().getGoogleNative());
                    AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_REWARD, response.body().getData().getGoogleReward() == null ? "" : response.body().getData().getGoogleReward());

                    if (AppUtility.getString(SplashActivity.this, Constant.GOOGLE_BANNER, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.GOOGLE_INT, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.GOOGLE_NATIVE, "").equalsIgnoreCase(""))
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, false);
                } else {
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, true);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, true);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_APP_AD_LINK, true);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_STARTAPP_AD, true);
                }
//                AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, false);
//                AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, false);
//                AppUtility.saveBoolean(SplashActivity.this, Constant.IS_APP_AD_LINK, false);
//                AppUtility.saveBoolean(SplashActivity.this, Constant.IS_STARTAPP_AD, false);

//                AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_NATIVE, "ca-app-pub-3940256099942544/2247696110");
//                AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_BANNER, "ca-app-pub-3940256099942544/6300978111");
//                AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_INT, "ca-app-pub-3940256099942544/1033173712");
//                AppUtility.saveString(SplashActivity.this, Constant.GOOGLE_REWARD, "ca-app-pub-3940256099942544/5354046379");

                getGlobalSettings();
            }

            @Override
            public void onFailure(@NonNull Call<AdsSettingModel> call, @NonNull Throwable t) {
                Log.e("API Failed", t.toString());
                getGlobalSettings();
            }
        });
    }

    private void getGlobalSettings() {
        Retrofit retrofit = ApiClient.getClient();
        ApiRest service = retrofit.create(ApiRest.class);
        Call<GlobalSettingModel> call = service.getSetting(getPackageName());
        call.enqueue(new Callback<GlobalSettingModel>() {
            @Override
            public void onResponse(@NonNull Call<GlobalSettingModel> call, @NonNull Response<GlobalSettingModel> response) {
                if (response.isSuccessful()) {
                    if (Objects.requireNonNull(response.body()).getData().getForcefullyUpdate() != null)
                        AppUtility.saveInt(SplashActivity.this, Constant.FORCEFULLY_UPDATE, Integer.parseInt(response.body().getData().getForcefullyUpdate()));
                    else
                        AppUtility.saveInt(SplashActivity.this, Constant.FORCEFULLY_UPDATE, BuildConfig.VERSION_CODE);
                    if (response.body().getData().getForcefullyUpdate() != null)
                        AppUtility.saveString(SplashActivity.this, Constant.FORCEFULLY_UPDATE_PKG, response.body().getData().getForcefullyUpdatePackage());
                    else
                        AppUtility.saveString(SplashActivity.this, Constant.FORCEFULLY_UPDATE_PKG, BuildConfig.APPLICATION_ID);
                } else {
                    AppUtility.saveInt(SplashActivity.this, Constant.FORCEFULLY_UPDATE, BuildConfig.VERSION_CODE);
                    AppUtility.saveString(SplashActivity.this, Constant.FORCEFULLY_UPDATE_PKG, BuildConfig.APPLICATION_ID);
                }
                launchActivity();
            }

            @Override
            public void onFailure(@NonNull Call<GlobalSettingModel> call, @NonNull Throwable t) {
                Log.e("API Failed", t.toString());
                launchActivity();
            }
        });
    }
}