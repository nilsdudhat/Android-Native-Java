package com.udemy.volume.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.udemy.volume.calculator.R;
import com.udemy.volume.calculator.adapters.ShapeAdapter;
import com.udemy.volume.calculator.models.Shape;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GridView gridView;

    ArrayList<Shape> shapeArrayList;

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

        gridView = findViewById(R.id.grid_view);

        getShapes();

        setUpGridView();
    }

    private void getShapes() {
        shapeArrayList = new ArrayList<>();

        Shape sphere = new Shape(R.drawable.sphere, "Sphere");
        Shape cylinder = new Shape(R.drawable.cylinder, "Cylinder");
        Shape prism = new Shape(R.drawable.prism, "Prism");
        Shape cube = new Shape(R.drawable.cube, "Cube");

        shapeArrayList.add(sphere);
        shapeArrayList.add(cylinder);
        shapeArrayList.add(prism);
        shapeArrayList.add(cube);
    }

    private void setUpGridView() {
        ShapeAdapter shapeAdapter = new ShapeAdapter(MainActivity.this, shapeArrayList);
        gridView.setAdapter(shapeAdapter);

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) { // considering sphere at 0 position
                Intent intent = new Intent(MainActivity.this, SphereActivity.class);
                startActivity(intent);
            } else if (position == 1) { // considering cylinder at 1 position
                Intent intent = new Intent(MainActivity.this, CylinderActivity.class);
                startActivity(intent);
            } else if (position == 2) { // considering prism at 2 position
                Intent intent = new Intent(MainActivity.this, PrismActivity.class);
                startActivity(intent);
            } else if (position == 3) { // considering cube at 3 position
                Intent intent = new Intent(MainActivity.this, CubeActivity.class);
                startActivity(intent);
            }
        });
    }
}