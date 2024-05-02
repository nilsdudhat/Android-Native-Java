package com.udemy.movie.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.udemy.movie.app.api.MovieRepository;
import com.udemy.movie.app.models.Movie;

import java.util.List;

/**
 * ViewModel : suitable for non-Android-specific logic
 * AndroidViewModel : used when ViewModel class needs to access Android-specific components
* */
public class MainActivityViewModel extends AndroidViewModel {

    private final MovieRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repository = new MovieRepository(application);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return repository.getMutableLiveData();
    }
}
