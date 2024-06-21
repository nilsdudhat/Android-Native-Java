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
import com.udemy.recyclerview.app.adapters.MultiViewAdapter;
import com.udemy.recyclerview.app.databinding.ActivityMultiViewItemRecyclerViewBinding;
import com.udemy.recyclerview.app.models.MultiEmployeeModel;

import java.util.ArrayList;

public class MultiViewItemRecyclerViewActivity extends AppCompatActivity {

    ActivityMultiViewItemRecyclerViewBinding binding;

    ArrayList<MultiEmployeeModel> multiEmployeeModelArrayList = new ArrayList<>();
    MultiViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMultiViewItemRecyclerViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        getListData();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        if (binding.rvMultiViewItem.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            binding.rvMultiViewItem.setLayoutManager(layoutManager);
            binding.rvMultiViewItem.addItemDecoration(new DividerItemDecoration(MultiViewItemRecyclerViewActivity.this, DividerItemDecoration.VERTICAL));
        }

        if (adapter == null) {
            adapter = new MultiViewAdapter();
            binding.rvMultiViewItem.setAdapter(adapter);
        }

        adapter.setMultiEmployeeModelArrayList(multiEmployeeModelArrayList);
    }

    private void getListData() {
        MultiEmployeeModel multiEmployeeModel1 = new MultiEmployeeModel();
        multiEmployeeModel1.setEmail("nils@gmail.com");
        multiEmployeeModelArrayList.add(multiEmployeeModel1);

        MultiEmployeeModel multiEmployeeModel2 = new MultiEmployeeModel();
        multiEmployeeModel2.setEmail("nilsdudhat@gmail.com");
        multiEmployeeModelArrayList.add(multiEmployeeModel2);

        MultiEmployeeModel multiEmployeeModel3 = new MultiEmployeeModel();
        multiEmployeeModel3.setMobileNumber("9876543210");
        multiEmployeeModelArrayList.add(multiEmployeeModel3);

        MultiEmployeeModel multiEmployeeModel4 = new MultiEmployeeModel();
        multiEmployeeModel4.setMobileNumber("5466543434");
        multiEmployeeModelArrayList.add(multiEmployeeModel4);

        MultiEmployeeModel multiEmployeeModel5 = new MultiEmployeeModel();
        multiEmployeeModel5.setEmail("absjcbdcv@nsnx.sbc");
        multiEmployeeModelArrayList.add(multiEmployeeModel5);
    }
}