package com.demo.combined.edittext;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import com.demo.combined.edittext.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.edtKilos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edtKilos.isCursorVisible() && binding.edtKilos.hasFocus()) {
                    if (!binding.edtKilos.getText().toString().isEmpty()) {
                        binding.edtPounds.setText(String.valueOf(convertKiloToPounds(Double.parseDouble(binding.edtKilos.getText().toString()))));
                    } else {
                        binding.edtPounds.setText("");
                    }
                }
            }
        });

        binding.edtPounds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edtPounds.isCursorVisible() && binding.edtPounds.hasFocus()) {
                    if (!binding.edtPounds.getText().toString().isEmpty()) {
                        binding.edtKilos.setText(String.valueOf(convertPoundToKilos(Double.parseDouble(binding.edtPounds.getText().toString()))));
                    } else {
                        binding.edtKilos.setText("");
                    }
                }
            }
        });
    }

    private double convertKiloToPounds(double kilos) {
        return kilos / 2.20462;
    }

    private double convertPoundToKilos(double pounds) {
        return pounds * 2.20462;
    }
}