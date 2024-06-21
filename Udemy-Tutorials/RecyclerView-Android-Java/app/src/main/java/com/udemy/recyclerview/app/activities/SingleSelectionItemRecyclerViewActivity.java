package com.udemy.recyclerview.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.recyclerview.app.R;
import com.udemy.recyclerview.app.adapters.SingleAdapter;
import com.udemy.recyclerview.app.databinding.ActivitySingleSelectionItemRecyclerViewBinding;
import com.udemy.recyclerview.app.models.SingleModel;

import java.util.ArrayList;

public class SingleSelectionItemRecyclerViewActivity extends AppCompatActivity implements SingleAdapter.SingleItemClickListener {

    ActivitySingleSelectionItemRecyclerViewBinding binding;

    SingleAdapter singleAdapter;
    ArrayList<SingleModel> singleModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySingleSelectionItemRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnGetSelectedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedPosition = -1;

                for (int i = 0; i < singleModelArrayList.size(); i++) {
                    SingleModel singleModel = singleModelArrayList.get(i);

                    if (singleModel.isChecked()) {
                        selectedPosition = i;
                        break;
                    }
                }

                int finalSelectedPosition = selectedPosition;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalSelectedPosition != -1) {
                            SingleModel singleModel = singleModelArrayList.get(finalSelectedPosition);

                            Toast.makeText(SingleSelectionItemRecyclerViewActivity.this, singleModel.getName() + "is selected", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SingleSelectionItemRecyclerViewActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
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
        if (binding.rvSingleSelection.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvSingleSelection.setLayoutManager(layoutManager);
        }

        if (singleAdapter == null) {
            singleAdapter = new SingleAdapter(this);
            binding.rvSingleSelection.setAdapter(singleAdapter);
        }
        singleAdapter.setSingleModelArrayList(singleModelArrayList);
    }

    @Override
    public void onSingleItemClick(int position) {
        int selectedPosition = -1;

        for (int i = 0; i < singleModelArrayList.size(); i++) {
            SingleModel singleModel = singleModelArrayList.get(i);

            if (singleModel.isChecked()) {
                selectedPosition = i;
                break;
            }
        }

        if (selectedPosition != -1) {
            SingleModel oldSelectedModel = singleModelArrayList.get(selectedPosition);
            oldSelectedModel.setChecked(false);

            singleModelArrayList.set(selectedPosition, oldSelectedModel);

            if (position != selectedPosition) {
                SingleModel singleModel = singleModelArrayList.get(position);
                singleModel.setChecked(true);

                singleModelArrayList.set(position, singleModel);
            }
        } else {
            SingleModel singleModel = singleModelArrayList.get(position);
            singleModel.setChecked(true);

            singleModelArrayList.set(position, singleModel);
        }

        singleAdapter.setSingleModelArrayList(singleModelArrayList);
    }
}