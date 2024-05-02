package com.udemy.movie.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.udemy.movie.app.R;
import com.udemy.movie.app.adapters.MovieAdapter;
import com.udemy.movie.app.databinding.ActivityMainBinding;
import com.udemy.movie.app.models.Movie;
import com.udemy.movie.app.viewmodels.MainActivityViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Movie> movieList;
    private MovieAdapter movieAdapter;
    private MainActivityViewModel viewModel;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.swipe_refresh), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupMovieRecyclerView();

        viewModel = new ViewModelProvider(MainActivity.this).get(MainActivityViewModel.class);

        getPopularMovies();

        binding.swipeRefresh.setColorSchemeResources(R.color.black);
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPopularMovies();
            }
        });
    }

    private void getPopularMovies() {
        binding.swipeRefresh.setRefreshing(true);
        viewModel.getAllMovies().observe(MainActivity.this, movies -> {
            movieList = movies;
            movieAdapter.setMovieList(movieList);
            binding.swipeRefresh.setRefreshing(false);
        });
    }

    private void setupMovieRecyclerView() {
        binding.rvMovies.setLayoutManager(new GridLayoutManager(MainActivity.this, 2));
        binding.rvMovies.setItemAnimator(new DefaultItemAnimator());
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter();
        }
        binding.rvMovies.setAdapter(movieAdapter);
    }
}