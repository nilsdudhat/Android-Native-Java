package com.udemy.paggination.app.api;

import com.udemy.paggination.app.models.MovieResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface the structure and behaviour of API requests
 * and acts as a bridge between API and App
 * */
public interface APIInterface {

    @GET("movie/popular")
    Single<MovieResponse> getMoviesByPage(@Query("page") int page);
}
