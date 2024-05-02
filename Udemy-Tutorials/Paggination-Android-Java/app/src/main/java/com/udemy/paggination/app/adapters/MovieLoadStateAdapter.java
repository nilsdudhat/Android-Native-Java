package com.udemy.paggination.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.paging.LoadState;
import androidx.paging.LoadStateAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.paggination.app.R;
import com.udemy.paggination.app.databinding.LoadStateItemBinding;

public class MovieLoadStateAdapter extends LoadStateAdapter<MovieLoadStateAdapter.ViewHolder> {

    View.OnClickListener retryClickListener;

    public MovieLoadStateAdapter(View.OnClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }

    @NonNull
    @Override
    public MovieLoadStateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, @NonNull LoadState loadState) {
        LoadStateItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(viewGroup.getContext()),
                R.layout.load_state_item,
                viewGroup,
                false);

        return new ViewHolder(binding, retryClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieLoadStateAdapter.ViewHolder viewHolder, @NonNull LoadState loadState) {
        viewHolder.bind(loadState);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LoadStateItemBinding binding;

        public ViewHolder(@NonNull LoadStateItemBinding binding, View.OnClickListener clickListener) {
            super(binding.getRoot());

            this.binding = binding;
        }

        public void bind(LoadState loadState) {
            binding.progressBar.setVisibility(
                    (loadState instanceof LoadState.Loading)
                            ? View.VISIBLE
                            : View.GONE);

            binding.btnRetry.setVisibility(
                    (loadState instanceof LoadState.Error)
                            ? View.VISIBLE
                            : View.GONE);

            binding.txtError.setVisibility(
                    (loadState instanceof LoadState.Error)
                            ? View.VISIBLE
                            : View.GONE);

            if (loadState instanceof LoadState.Error) {
                LoadState.Error error = (LoadState.Error) loadState;
                binding.txtError.setText(error.getError().getLocalizedMessage());
            }
        }
    }
}
