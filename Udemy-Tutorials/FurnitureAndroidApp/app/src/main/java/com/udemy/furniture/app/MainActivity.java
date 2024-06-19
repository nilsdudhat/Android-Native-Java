package com.udemy.furniture.app;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.udemy.furniture.app.adapters.DataAdapter;
import com.udemy.furniture.app.adapters.SellingItemAdapter;
import com.udemy.furniture.app.data.MyData;
import com.udemy.furniture.app.data.SellingData;
import com.udemy.furniture.app.databinding.ActivityMainBinding;
import com.udemy.furniture.app.models.DataModel;
import com.udemy.furniture.app.models.SellingModel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        initMainCards();
        initSellingItems();
    }

    private void initSellingItems() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvSellingItems.setLayoutManager(layoutManager);
        binding.rvSellingItems.setItemAnimator(new DefaultItemAnimator());

        ArrayList<SellingModel> dataModelArrayList = new ArrayList<>();
        for (int i = 0; i < SellingData.titleArray.length; i++) {
            dataModelArrayList.add(new SellingModel(
                    SellingData.titleArray[i],
                    SellingData.subTitleArray[i],
                    SellingData.prices[i],
                    SellingData.dataImages[i]
            ));
        }

        SellingItemAdapter adapter = new SellingItemAdapter();
        binding.rvSellingItems.setAdapter(adapter);

        adapter.setSellingModelArrayList(dataModelArrayList);
    }

    private void initMainCards() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rvCards.setLayoutManager(layoutManager);
        binding.rvCards.setItemAnimator(new DefaultItemAnimator());

        ArrayList<DataModel> dataModelArrayList = new ArrayList<>();
        for (int i = 0; i < MyData.nameArray.length; i++) {
            dataModelArrayList.add(new DataModel(
                    MyData.nameArray[i],
                    MyData.versionArray[i],
                    MyData.ids[i],
                    MyData.dataImages[i]
            ));
        }

        DataAdapter adapter = new DataAdapter();
        binding.rvCards.setAdapter(adapter);

        adapter.setDataArrayList(dataModelArrayList);
    }
}