package com.udemy.java.market.app.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.java.market.app.R;
import com.udemy.java.market.app.adapters.ItemAdapter;
import com.udemy.java.market.app.interfaces.ItemClickListener;
import com.udemy.java.market.app.models.Item;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ItemClickListener {

    RecyclerView rvList;

    List<Item> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        rvList = findViewById(R.id.rv_list);

        getItemList();

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rvList.setLayoutManager(layoutManager);

        ItemAdapter itemAdapter = new ItemAdapter(itemList, MainActivity.this);
        rvList.setAdapter(itemAdapter);
    }

    private void getItemList() {
        itemList = new ArrayList<>();

        Item fruit = new Item(R.drawable.fruit, "Fruits", "Fresh Fruits from the Garden");
        Item vegetables = new Item(R.drawable.vegitables, "Vegetables", "Delicious Vegetables ");
        Item bread = new Item(R.drawable.bread, "Bakery", "Bread, Wheat and Beans");
        Item beverage = new Item(R.drawable.beverage, "Beverage", "Juice, Tea, Coffee and Soda");
        Item milk = new Item(R.drawable.milk, "Milk", "Milk, Shakes and Yogurt");
        Item popcorn = new Item(R.drawable.popcorn, "Snacks", "Pop Corn, Donut and Drinks");

        itemList.add(fruit);
        itemList.add(vegetables);
        itemList.add(bread);
        itemList.add(beverage);
        itemList.add(milk);
        itemList.add(popcorn);
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(this, "You clicked - " + itemList.get(position).getItemName(), Toast.LENGTH_SHORT).show();
    }
}