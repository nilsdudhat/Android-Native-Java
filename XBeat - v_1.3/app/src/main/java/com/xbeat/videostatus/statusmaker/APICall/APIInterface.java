package com.xbeat.videostatus.statusmaker.APICall;

import com.xbeat.videostatus.statusmaker.Models.ModelVideoListData;
import com.xbeat.videostatus.statusmaker.Models.ModelVideoListDataByCategory;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIInterface {

    @POST("get-templates")
    Call<ModelVideoListData> getVideoList(@Query("sort_by") String str);

    @POST("get-cat-templates")
    Call<ModelVideoListDataByCategory> getVideoListByCategory(@Query("sort_by") String str, @Query("cat_id") String str2);
}
