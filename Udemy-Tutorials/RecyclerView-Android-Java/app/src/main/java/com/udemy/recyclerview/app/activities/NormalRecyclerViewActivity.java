package com.udemy.recyclerview.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.recyclerview.app.R;
import com.udemy.recyclerview.app.adapters.NormalAdapter;
import com.udemy.recyclerview.app.databinding.ActivityNormalRecyclerViewBinding;
import com.udemy.recyclerview.app.models.PlanetModel;

import java.util.ArrayList;

public class NormalRecyclerViewActivity extends AppCompatActivity {

    ArrayList<PlanetModel> planetModelArrayList = new ArrayList<>();

    ActivityNormalRecyclerViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityNormalRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getPlanetData();
        setUpRecyclerView();
    }

    private void getPlanetData() {
        planetModelArrayList.add(new PlanetModel("Mercury", 58, 3.7, 4879));
        planetModelArrayList.add(new PlanetModel("Venus", 108, 8.9, 12104));
        planetModelArrayList.add(new PlanetModel("Earth", 93, 9.8, 12742));
        planetModelArrayList.add(new PlanetModel("Mars", 228, 3.7, 6779));
        planetModelArrayList.add(new PlanetModel("Jupiter", 778, 24.79, 142984));
        planetModelArrayList.add(new PlanetModel("Saturn", 1427, 10.44, 120536));
        planetModelArrayList.add(new PlanetModel("Uranus", 2871, 8.87, 51118));
        planetModelArrayList.add(new PlanetModel("Neptune", 4498, 11.15, 49528));
        planetModelArrayList.add(new PlanetModel("Pluto", 4600, 0.62, 2370));
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                NormalRecyclerViewActivity.this, LinearLayoutManager.VERTICAL, false);
        binding.rvPlanets.setLayoutManager(layoutManager);
        binding.rvPlanets.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        NormalAdapter normalAdapter = new NormalAdapter();
        binding.rvPlanets.setAdapter(normalAdapter);

        normalAdapter.setPlanetModelArrayList(planetModelArrayList);
    }
}