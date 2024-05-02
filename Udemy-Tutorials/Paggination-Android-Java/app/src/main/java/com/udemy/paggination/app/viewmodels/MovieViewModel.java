package com.udemy.paggination.app.viewmodels;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.rxjava3.PagingRx;

import com.udemy.paggination.app.models.Movie;
import com.udemy.paggination.app.paging.MoviePagingSource;

import io.reactivex.rxjava3.core.Flowable;
import kotlinx.coroutines.CoroutineScope;

public class MovieViewModel extends ViewModel {

    public Flowable<PagingData<Movie>> flowable;

    public MovieViewModel() {
        init();
    }

    private void init() {
        // define paging source
        MoviePagingSource pagingSource = new MoviePagingSource();
        Pager<Integer, Movie> pager = new Pager<>(
                new PagingConfig(20,
                        20,
                        false,
                        20,
                        20 * 499),
                () -> pagingSource);

        flowable = PagingRx.getFlowable(pager);
        CoroutineScope coroutineScope = ViewModelKt.getViewModelScope(this);
        PagingRx.cachedIn(flowable, coroutineScope);
    }
}
