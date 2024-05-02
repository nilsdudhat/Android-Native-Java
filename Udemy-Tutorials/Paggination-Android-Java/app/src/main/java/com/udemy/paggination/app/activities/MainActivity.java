package com.udemy.paggination.app.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.RequestManager;
import com.udemy.paggination.app.R;
import com.udemy.paggination.app.adapters.MovieAdapter;
import com.udemy.paggination.app.adapters.MovieLoadStateAdapter;
import com.udemy.paggination.app.databinding.ActivityMainBinding;
import com.udemy.paggination.app.utils.Constants;
import com.udemy.paggination.app.utils.GridSpace;
import com.udemy.paggination.app.utils.MovieComparator;
import com.udemy.paggination.app.viewmodels.MovieViewModel;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    private MovieAdapter movieAdapter;
    private ActivityMainBinding binding;

    @Inject
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.constraint_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if ((Constants.API_KEY == null) || Constants.API_KEY.isEmpty()) {
            Toast.makeText(this, "API key is not proper", Toast.LENGTH_SHORT).show();
            return;
        }
        setupMovieRecyclerView();

        MovieViewModel movieViewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        movieViewModel.flowable.subscribe(moviePagingData -> {
            movieAdapter.submitData(getLifecycle(), moviePagingData);
        });
    }

    private void setupMovieRecyclerView() {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(new MovieComparator(), requestManager);
        }

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (movieAdapter.getItemViewType(position) == MovieAdapter.LOADING_ITEM)
                        ? 1
                        : 2;
            }
        });
        binding.rvMovies.setLayoutManager(layoutManager);

        binding.rvMovies.setItemAnimator(new DefaultItemAnimator());
        binding.rvMovies.addItemDecoration(new GridSpace(2, 20, true));

        binding.rvMovies.setAdapter(
                movieAdapter.withLoadStateFooter(
                        new MovieLoadStateAdapter(v -> {
                            movieAdapter.retry();
                        })));
    }
}