package com.xbeat.videostatus.statusmaker.AdUtils;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiRest {
    @FormUrlEncoded
    @POST(Constant.SETTING)
    Call<GlobalSettingModel> getSetting(@Field("package_name") String package_name);

    @FormUrlEncoded
    @POST(Constant.ADS_SETTING)
    Call<AdsSettingModel> getAdsSettings(@Field("package_name") String package_name);
}