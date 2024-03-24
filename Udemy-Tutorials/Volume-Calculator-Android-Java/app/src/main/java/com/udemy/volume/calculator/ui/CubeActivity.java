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

public class CubeActivity extends AppCompatActivity {

    Button btnCalculate;
    TextView txtResult;
    TextInputEditText editLength;
    TextInputLayout lengthContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cube);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        editLength = findViewById(R.id.edit_legth);
        lengthContainer = findViewById(R.id.length_container);
        txtResult = findViewById(R.id.txt_result);
        btnCalculate = findViewById(R.id.btn_calculate);

        editLength.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lengthContainer.setError("");
            }
        });

        btnCalculate.setOnClickListener(v -> {
            if (editLength.getText() == null) {
                return;
            }
            if (editLength.getText().toString().isEmpty()) {
                lengthContainer.setError("");
                return;
            }
            // V = length ^ 3;

            double length = Double.parseDouble(editLength.getText().toString());
            double volume = length * length * length;

            txtResult.setText((new StringBuilder())
                    .append("Result: ")
                    .append(volume)
                    .append(" m^3"));
        });
    }
}