package com.udemy.volume.calculator.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.udemy.volume.calculator.R;

public class CylinderActivity extends AppCompatActivity {

    Button btnCalculate;
    TextView txtResult;
    TextInputEditText editRadius;
    TextInputLayout radiusContainer;
    TextInputEditText editHeight;
    TextInputLayout heightContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cylinder);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        editHeight = findViewById(R.id.edit_height);
        heightContainer = findViewById(R.id.height_container);
        editRadius = findViewById(R.id.edit_legth);
        radiusContainer = findViewById(R.id.length_container);
        txtResult = findViewById(R.id.txt_result);
        btnCalculate = findViewById(R.id.btn_calculate);

        editRadius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                radiusContainer.setError("");
            }
        });

        editHeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                heightContainer.setError("");
            }
        });

        btnCalculate.setOnClickListener(v -> {
            if ((editRadius.getText() == null) || (editHeight.getText() == null)) {
                return;
            }
            if (editRadius.getText().toString().isEmpty()) {
                radiusContainer.setError("");
                return;
            }
            if (editHeight.getText().toString().isEmpty()) {
                heightContainer.setError("");
                return;
            }
            // V = 3.14159 * r * r * h;

            double radius = Double.parseDouble(editRadius.getText().toString());
            double height = Double.parseDouble(editHeight.getText().toString());
            double volume = 3.14159 * radius * radius * height;

            txtResult.setText((new StringBuilder())
                    .append("Result: ")
                    .append(volume)
                    .append(" m^3"));
        });
    }
}