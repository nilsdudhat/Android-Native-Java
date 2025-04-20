package com.allvideo.hdplayer.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.allvideo.hdplayer.AdsIntegration.AdsSettingModel;
import com.allvideo.hdplayer.AdsIntegration.ApiClient;
import com.allvideo.hdplayer.AdsIntegration.ApiRest;
import com.allvideo.hdplayer.AdsIntegration.AppUtility;
import com.allvideo.hdplayer.AdsIntegration.Constant;
import com.allvideo.hdplayer.AdsIntegration.GlobalSettingModel;
import com.allvideo.hdplayer.BuildConfig;
import com.allvideo.hdplayer.R;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 12564;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);

        getSettings();
    }

    private void launchActivity() {
        new Handler(Looper.myLooper()).postDelayed(() -> {

            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            } else {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2500);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        System.runFinalization();
        Runtime.getRuntime().gc();
        System.gc();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(SplashActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(SplashActivity.this, "Please Allow", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
            }
        }
    }

    private void getSettings() {
        Retrofit retrofit = ApiClient.getClient();
        ApiRest service = retrofit.create(ApiRest.class);
        Call<AdsSettingModel> call = service.getAdsSettings(getPackageName());

        call.enqueue(new Callback<AdsSettingModel>() {
            @Override
            public void onResponse(@NotNull Call<AdsSettingModel> call, @NotNull Response<AdsSettingModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    AppUtility.saveString(SplashActivity.this, Constant.COUNTER, response.body().getData().getCounter() == null ? "5" : response.body().getData().getCounter());

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

                    if (AppUtility.getString(SplashActivity.this, Constant.GOOGLE_BANNER, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.GOOGLE_INT, "").equalsIgnoreCase("") || AppUtility.getString(SplashActivity.this, Constant.GOOGLE_NATIVE, "").equalsIgnoreCase(""))
                        AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, false);
                } else {
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_GOOGLE_AD, false);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_FACEBOOK_AD, false);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_APP_AD_LINK, false);
                    AppUtility.saveBoolean(SplashActivity.this, Constant.IS_STARTAPP_AD, false);
                }

                getGlobalSettings();
            }

            @Override
            public void onFailure(@NotNull Call<AdsSettingModel> call, @NotNull Throwable t) {
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
            public void onResponse(Call<GlobalSettingModel> call, Response<GlobalSettingModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().getData().getForcefullyUpdate() != null)
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
            public void onFailure(@NotNull Call<GlobalSettingModel> call, @NotNull Throwable t) {
                Log.e("API Failed", t.toString());
                launchActivity();
            }
        });
    }
}
