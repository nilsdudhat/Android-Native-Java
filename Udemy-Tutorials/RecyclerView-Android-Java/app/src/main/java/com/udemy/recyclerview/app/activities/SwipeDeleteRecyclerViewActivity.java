package com.udemy.recyclerview.app.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.recyclerview.app.R;
import com.udemy.recyclerview.app.adapters.NormalAdapter;
import com.udemy.recyclerview.app.adapters.SwipeAdapter;
import com.udemy.recyclerview.app.databinding.ActivitySwipeDeleteRecyclerViewBinding;
import com.udemy.recyclerview.app.models.PlanetModel;

import java.util.ArrayList;

public class SwipeDeleteRecyclerViewActivity extends AppCompatActivity {

    ActivitySwipeDeleteRecyclerViewBinding binding;

    ArrayList<PlanetModel> planetModelArrayList = new ArrayList<>();
    SwipeAdapter swipeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySwipeDeleteRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getPlanetData();
        setUpRecyclerView();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();

            if (direction == ItemTouchHelper.LEFT) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SwipeDeleteRecyclerViewActivity.this); //alert for confirm to delete
                builder.setMessage("Are you sure to delete?");

                builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swipeAdapter.notifyItemRemoved(position);
                        planetModelArrayList.remove(position);
                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        swipeAdapter.notifyItemChanged(position);
                    }
                }).show();
            }
        }
    };

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
        if (binding.rvSwipeDelete.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                    this, LinearLayoutManager.VERTICAL, false);
            binding.rvSwipeDelete.setLayoutManager(layoutManager);
            binding.rvSwipeDelete.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        }

        if (swipeAdapter == null) {
            swipeAdapter = new SwipeAdapter();
            binding.rvSwipeDelete.setAdapter(swipeAdapter);
        }

        swipeAdapter.setPlanetModelArrayList(planetModelArrayList);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvSwipeDelete);
    }
}