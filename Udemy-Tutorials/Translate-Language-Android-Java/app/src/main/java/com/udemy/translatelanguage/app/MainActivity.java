package com.udemy.translatelanguage.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;
import com.udemy.translatelanguage.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    String[] sourceLanguages = {"---from---", "Afrikaans", "English", "Arabic", "Hindi", "Gujarati", "Urdu", "Russian"};
    String[] targetLanguages = {"---to---", "Afrikaans", "English", "Arabic", "Hindi", "Gujarati", "Urdu", "Russian"};

    String sourceLanguageCode, targetLanguageCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupSourceSpinner();
        setupTargetSpinner();

        binding.btnTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtTranslated.setText("");

                if (TextUtils.isEmpty(binding.edtInput.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Please enter text first", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(sourceLanguageCode)) {
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();
                } else if (sourceLanguageCode.equals("---from---")) {
                    Toast.makeText(MainActivity.this, "Please select source language", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(targetLanguageCode)) {
                    Toast.makeText(MainActivity.this, "Please select target language", Toast.LENGTH_SHORT).show();
                } else if (targetLanguageCode.equals("---to---")) {
                    Toast.makeText(MainActivity.this, "Please select target language", Toast.LENGTH_SHORT).show();
                } else {
                    getTranslatedText(binding.edtInput.getText().toString(), sourceLanguageCode, targetLanguageCode).observe(MainActivity.this, new Observer<String>() {
                        @Override
                        public void onChanged(String s) {
                            binding.txtTranslated.setText(s);
                        }
                    });
                }
            }
        });
    }

    private void setupTargetSpinner() {
        binding.spinnerTarget.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                targetLanguageCode = getLanguageCode(targetLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter targetAdapter = new ArrayAdapter(MainActivity.this, R.layout.item_spinner, targetLanguages);
        targetAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerTarget.setAdapter(targetAdapter);
    }

    private void setupSourceSpinner() {
        binding.spinnerSource.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sourceLanguageCode = getLanguageCode(sourceLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter sourceAdapter = new ArrayAdapter(MainActivity.this, R.layout.item_spinner, sourceLanguages);
        sourceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSource.setAdapter(sourceAdapter);
    }

    private String getLanguageCode(String language) {
        String languageCode;
        switch (language) {
            case "English":
                languageCode = TranslateLanguage.ENGLISH;
                break;
            case "Afrikaans":
                languageCode = TranslateLanguage.AFRIKAANS;
                break;
            case "Arabic":
                languageCode = TranslateLanguage.ARABIC;
                break;
            case "Hindi":
                languageCode = TranslateLanguage.HINDI;
                break;
            case "Urdu":
                languageCode = TranslateLanguage.URDU;
                break;
            case "Russian":
                languageCode = TranslateLanguage.RUSSIAN;
                break;
            case "Gujarati":
                languageCode = TranslateLanguage.GUJARATI;
                break;
            default:
                languageCode = "";
        }
        return languageCode;
    }

    private MutableLiveData<String> getTranslatedText(String text, String sourceLanguage, String targetLanguage) {
        MutableLiveData<String> liveData = new MutableLiveData<>();
        liveData.setValue("Downloading Language Model");

        TranslatorOptions translatorOptions = new TranslatorOptions.Builder()
                .setSourceLanguage(sourceLanguage)
                .setTargetLanguage(targetLanguage)
                .build();

        Translator translator = Translation.getClient(translatorOptions);

        DownloadConditions conditions
                = new DownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        liveData.setValue("Translating");

                        translator.translate(text)
                                .addOnSuccessListener(new OnSuccessListener<String>() {
                                    @Override
                                    public void onSuccess(String s) {
                                        liveData.setValue(s);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        liveData.setValue("Failed to translate given text");
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        liveData.setValue("Failed to download language model");
                    }
                });

        return liveData;
    }
}