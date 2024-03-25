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

public class PrismActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prism);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
    }

    private void initViews() {

        TextInputEditText editArea = findViewById(R.id.edit_area);
        TextInputLayout areaContainer = findViewById(R.id.area_container);
        TextInputEditText editHeight = findViewById(R.id.edit_height);
        TextInputLayout heightContainer = findViewById(R.id.height_container);
        TextView txtResult = findViewById(R.id.txt_result);
        Button btnCalculate = findViewById(R.id.btn_calculate);

        editArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                areaContainer.setError("");
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
            if ((editArea.getText() == null) || (editHeight.getText() == null)) {
                return;
            }
            if (editArea.getText().toString().isEmpty()) {
                areaContainer.setError("");
                return;
            }
            if (editHeight.getText().toString().isEmpty()) {
                heightContainer.setError("");
                return;
            }
            // V = area * height;

            double area = Double.parseDouble(editArea.getText().toString());
            double height = Double.parseDouble(editHeight.getText().toString());
            double volume = area * height;

            txtResult.setText((new StringBuilder())
                    .append("Result: ")
                    .append(volume)
                    .append(" m^3"));
        });
    }
}