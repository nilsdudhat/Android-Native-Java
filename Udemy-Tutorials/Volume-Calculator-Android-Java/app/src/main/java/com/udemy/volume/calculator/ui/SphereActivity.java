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

public class SphereActivity extends AppCompatActivity {

    Button btnCalculate;
    TextView txtResult;
    TextInputEditText edtRadius;
    TextInputLayout textInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sphere);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {
        textInputLayout = findViewById(R.id.length_container);
        edtRadius = findViewById(R.id.edit_text);
        txtResult = findViewById(R.id.txt_result);
        btnCalculate = findViewById(R.id.btn_calculate);

        edtRadius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputLayout.setError("");
            }
        });

        btnCalculate.setOnClickListener(v -> {
            if ((edtRadius.getText() == null)
                    || (edtRadius.getText().toString().isEmpty())) {
                textInputLayout.setError("Enter Radius");

                return;
            }
            // V = (4/3) * 3.14159 * r * r *r;

            double radius = Double.parseDouble(edtRadius.getText().toString());
            double volume = ((double) 4 / 3) * 3.14159 * radius * radius * radius;

            txtResult.setText((new StringBuilder())
                    .append("Result: ")
                    .append(volume)
                    .append(" m^3"));
        });
    }
}