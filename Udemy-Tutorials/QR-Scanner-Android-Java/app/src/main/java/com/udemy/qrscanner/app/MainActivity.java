package com.udemy.qrscanner.app;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.core.resolutionselector.ResolutionStrategy;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.udemy.qrscanner.app.databinding.ActivityMainBinding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    ListenableFuture cameraProviderFuture;
    QRImageAnalyser qrImageAnalyser;
    ExecutorService cameraExecutor;

    int PERMISSION_REQUEST_CODE = 1023;

    ActivityMainBinding binding;

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

        cameraExecutor = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(MainActivity.this);

        qrImageAnalyser = new QRImageAnalyser(getApplicationContext(), getSupportFragmentManager());

        cameraProviderFuture.addListener(
                new Runnable() {
                    @Override
                    public void run() {
                        // background job

                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                                != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(
                                    MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA},
                                    PERMISSION_REQUEST_CODE);
                        } else {
                            try {
                                ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                                bindPreview(processCameraProvider);
                            } catch (Exception e) {
                                Log.d("--catch--", "processCameraProvider: " + e.getMessage());
                            }
                        }
                    }
                },
                ContextCompat.getMainExecutor(MainActivity.this));
    }

    private void bindPreview(ProcessCameraProvider processCameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder().build();

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setResolutionSelector(
                        new ResolutionSelector.Builder()
                                .setResolutionStrategy(new ResolutionStrategy(
                                        new Size(1280, 720),
                                        ResolutionStrategy.FALLBACK_RULE_NONE))
                                .build())
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();
        imageAnalysis.setAnalyzer(cameraExecutor, qrImageAnalyser);
        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);
    }

    @Override
    protected void onDestroy() {
        cameraExecutor.shutdown();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if ((requestCode == PERMISSION_REQUEST_CODE) && (grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            try {
                ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                bindPreview(processCameraProvider);
            } catch (Exception e) {
                Log.d("--catch--", "processCameraProvider: " + e.getMessage());
            }
        }
    }
}