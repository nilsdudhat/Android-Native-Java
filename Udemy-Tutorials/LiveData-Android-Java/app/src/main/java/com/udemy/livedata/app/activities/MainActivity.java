package com.udemy.livedata.app.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.udemy.livedata.app.R;
import com.udemy.livedata.app.data.CounterViewModel;
import com.udemy.livedata.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    CounterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        viewModel = new ViewModelProvider(MainActivity.this)
                .get(CounterViewModel.class);

        binding.setCounter(viewModel);

        // observing livedata for changes
        viewModel.getCounter().observe(MainActivity.this, integer -> {

            // update UI on data change
            binding.txtCounter.setText(
                    new StringBuilder().append((integer != null) ? integer : 0));
        });

        // to display default value
        binding.txtCounter.setText(
                new StringBuilder().append((viewModel.getCounter().getValue() != null)
                        ? viewModel.getCounter().getValue()
                        : 0));
    }
}