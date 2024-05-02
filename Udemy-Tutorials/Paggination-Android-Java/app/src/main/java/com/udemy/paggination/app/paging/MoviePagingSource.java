package com.udemy.paggination.app.paging;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingState;
import androidx.paging.rxjava3.RxPagingSource;

import com.udemy.paggination.app.api.APIClient;
import com.udemy.paggination.app.models.Movie;
import com.udemy.paggination.app.models.MovieResponse;

import java.util.List;

import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MoviePagingSource extends RxPagingSource<Integer, Movie> {

    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, Movie> pagingState) {
        return null;
    }

    @NonNull
    @Override
    public Single<LoadResult<Integer, Movie>> loadSingle(@NonNull LoadParams<Integer> loadParams) {
        try {
            int page = (loadParams.getKey() != null) ? loadParams.getKey() : 1;

            Single<LoadResult<Integer, Movie>> resultSingle = APIClient.getApiInterface()
                    .getMoviesByPage(page)
                    .subscribeOn(Schedulers.io())
                    .map(MovieResponse::getMovieList)
                    .map(movies -> toLoadResult(movies, page))
                    .onErrorReturn(LoadResult.Error::new);

            return resultSingle;
        } catch (Exception e) {
            Log.d("--catch--", "loadSingle: " + e.getMessage());
            return Single.just(new LoadResult.Error<>(e));
        }
    }

    private LoadResult<Integer, Movie> toLoadResult(List<Movie> movies, int page) {
        return new LoadResult.Page<>(movies, page == 1 ? null : page - 1, page + 1);
    }
}
