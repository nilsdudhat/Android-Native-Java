package com.udemy.java.sports.app.activities;

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

import com.udemy.java.sports.app.R;
import com.udemy.java.sports.app.SportClickListener;
import com.udemy.java.sports.app.adapters.SportAdapter;
import com.udemy.java.sports.app.models.Sport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SportClickListener {

    RecyclerView rvSports;

    List<Sport> sportList;

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

        rvSports = findViewById(R.id.rv_sports);

        getSportsList();

        setupRecyclerView();
    }

    private void setupRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rvSports.setLayoutManager(layoutManager);

        SportAdapter adapter = new SportAdapter(sportList, MainActivity.this);
        rvSports.setAdapter(adapter);
    }

    private void getSportsList() {
        sportList = new ArrayList<>();

        Sport football = new Sport("Football", R.drawable.football);
        Sport basketball = new Sport("Basketball", R.drawable.basketball);
        Sport volleyball = new Sport("Volleyball", R.drawable.volley);
        Sport tennis = new Sport("Tennis", R.drawable.tennis);
        Sport ping = new Sport("Ping", R.drawable.ping);

        sportList.add(football);
        sportList.add(basketball);
        sportList.add(volleyball);
        sportList.add(tennis);
        sportList.add(ping);
    }

    @Override
    public void onClick(View view, int position) {
        Toast.makeText(this, "You clicked: " + sportList.get(position).getSportName(), Toast.LENGTH_SHORT).show();
    }
}