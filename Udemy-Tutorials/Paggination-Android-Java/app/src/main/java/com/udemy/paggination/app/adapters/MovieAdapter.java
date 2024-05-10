package com.udemy.paggination.app.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.RequestManager;
import com.udemy.paggination.app.R;
import com.udemy.paggination.app.databinding.ItemMovieBinding;
import com.udemy.paggination.app.models.Movie;

public class MovieAdapter extends PagingDataAdapter<Movie, MovieAdapter.ViewHolder> {

    public static final int LOADING_ITEM = 0;
    public static final int MOVIE_ITEM = 0;

    public MovieAdapter(@NonNull DiffUtil.ItemCallback<Movie> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_movie, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapter.ViewHolder holder, int position) {
        Movie movie = getItem(position);
        holder.binding.setMovie(movie);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == getItemCount()) ? MOVIE_ITEM : LOADING_ITEM;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ItemMovieBinding binding;

        public ViewHolder(@NonNull ItemMovieBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
