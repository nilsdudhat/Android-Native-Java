package com.udemy.movie.app.api;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.udemy.movie.app.R;
import com.udemy.movie.app.models.Movie;
import com.udemy.movie.app.models.MovieResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * used to extract data source details and
 * provides a clean API for the ViewModel to fetch and manage data
* */
public class MovieRepository {

    private List<Movie> movieList = new ArrayList<>();
    private final MutableLiveData<List<Movie>> mutableLiveData = new MutableLiveData<>();
    private final Application application;

    public MovieRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Movie>> getMutableLiveData() {
        APIInterface apiInterface = APIClient.getMovieAPIRest();

        Call<MovieResponse> call = apiInterface.getPopularMovies(application.getApplicationContext().getString(R.string.api_key));

        /*
        * enqueue :
        * perform network request in the background thread and
        * handle the response on the main (UI) thread
        *
        * execute :
        * perform network request in the main thread,
        * which can cause the stuck of the UI
        * */
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                // Success
                if (response.isSuccessful()) {
                    MovieResponse movieResponse = response.body();

                    if ((movieResponse != null) && movieResponse.getMovieList() != null) {
                        movieList = movieResponse.getMovieList();
                        mutableLiveData.setValue(movieList);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable throwable) {

            }
        });

        return mutableLiveData;
    }
}
