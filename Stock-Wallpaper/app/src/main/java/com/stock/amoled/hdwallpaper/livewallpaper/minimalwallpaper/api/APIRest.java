package com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.api;

import com.stock.amoled.hdwallpaper.livewallpaper.minimalwallpaper.models.Wallpaper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIRest {

    @GET("image/get-all")
    Call<Wallpaper> getAllWallpapers(@Query("pageNumber") int pageNumber,
                                     @Query("pageSize") int pageSize);
}
