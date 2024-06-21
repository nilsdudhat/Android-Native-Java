package com.udemy.recyclerview.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.recyclerview.app.R;
import com.udemy.recyclerview.app.adapters.SingleAdapter;
import com.udemy.recyclerview.app.databinding.ActivityMultiSelectRecyclerViewBinding;
import com.udemy.recyclerview.app.models.SingleModel;

import java.util.ArrayList;

public class MultiSelectRecyclerViewActivity extends AppCompatActivity implements SingleAdapter.SingleItemClickListener {

    ActivityMultiSelectRecyclerViewBinding binding;

    SingleAdapter singleAdapter;
    ArrayList<SingleModel> singleModelArrayList = new ArrayList<>();
    ArrayList<Integer> selectedPositionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMultiSelectRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getListData();
        setUpRecyclerView();
    }

    private void getListData() {
        singleModelArrayList.add(new SingleModel(false, "Employee 1"));
        singleModelArrayList.add(new SingleModel(false, "Employee 2"));
        singleModelArrayList.add(new SingleModel(false, "Employee 3"));
        singleModelArrayList.add(new SingleModel(false, "Employee 4"));
        singleModelArrayList.add(new SingleModel(false, "Employee 5"));
        singleModelArrayList.add(new SingleModel(false, "Employee 6"));
        singleModelArrayList.add(new SingleModel(false, "Employee 7"));
        singleModelArrayList.add(new SingleModel(false, "Employee 8"));
        singleModelArrayList.add(new SingleModel(false, "Employee 9"));
        singleModelArrayList.add(new SingleModel(false, "Employee 10"));
        singleModelArrayList.add(new SingleModel(false, "Employee 11"));
        singleModelArrayList.add(new SingleModel(false, "Employee 12"));
        singleModelArrayList.add(new SingleModel(false, "Employee 13"));
        singleModelArrayList.add(new SingleModel(false, "Employee 14"));
        singleModelArrayList.add(new SingleModel(false, "Employee 15"));
    }

    private void setUpRecyclerView() {
        if (binding.rvMultiSelection.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvMultiSelection.setLayoutManager(layoutManager);
        }

        if (singleAdapter == null) {
            singleAdapter = new SingleAdapter(this);
            binding.rvMultiSelection.setAdapter(singleAdapter);
        }
        singleAdapter.setSingleModelArrayList(singleModelArrayList);
    }

    @Override
    public void onSingleItemClick(int position) {
        if (selectedPositionList.contains(position)) {
            selectedPositionList.remove((Integer) position);
        } else {
            selectedPositionList.add(position);
        }

        for (int i = 0; i < singleModelArrayList.size(); i++) {
            SingleModel singleModel = singleModelArrayList.get(i);
            singleModel.setChecked(selectedPositionList.contains(i));
            singleModelArrayList.set(i, singleModel);
        }

        singleAdapter.setSingleModelArrayList(singleModelArrayList);
    }
}