package com.udemy.planet.listview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView list_planet;
    ArrayList<PlanetModel> planetModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list_planet = findViewById(R.id.list_planet);

        PlanetModel mercury = new PlanetModel("Mercury", 0, R.drawable.mercury);
        PlanetModel venus = new PlanetModel("Venus", 0, R.drawable.venus);
        PlanetModel earth = new PlanetModel("Earth", 1, R.drawable.earth);
        PlanetModel mars = new PlanetModel("Mars", 2, R.drawable.mars);
        PlanetModel jupiter = new PlanetModel("Jupiter", 79, R.drawable.jupiter);
        PlanetModel saturn = new PlanetModel("Saturn", 83, R.drawable.saturn);
        PlanetModel uranus = new PlanetModel("Uranus", 27, R.drawable.uranus);
        PlanetModel neptune = new PlanetModel("Neptune", 14, R.drawable.neptune);

        planetModelArrayList.add(mercury);
        planetModelArrayList.add(venus);
        planetModelArrayList.add(earth);
        planetModelArrayList.add(mars);
        planetModelArrayList.add(jupiter);
        planetModelArrayList.add(saturn);
        planetModelArrayList.add(uranus);
        planetModelArrayList.add(neptune);

        PlanetAdapter planetAdapter = new PlanetAdapter(getApplicationContext(), planetModelArrayList);
        list_planet.setAdapter(planetAdapter);

        list_planet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this,
                                "Planet Name: " + planetModelArrayList.get(position).getPlanetName(),
                                Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}