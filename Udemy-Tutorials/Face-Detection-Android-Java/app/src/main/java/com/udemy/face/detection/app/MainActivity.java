package com.udemy.face.detection.app;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.udemy.face.detection.app.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    int PERMISSION_CODE = 1234;

    ActivityResultLauncher<Intent> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        if (data != null) {
                            Bundle bundle = data.getExtras();
                            if (bundle != null) {
                                Bitmap bitmap = (Bitmap) bundle.get("data");
                                binding.imgSelected.setImageBitmap(bitmap);

                                detectFaces(bitmap);
                            }
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseApp.initializeApp(this);

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, PERMISSION_CODE);
                } else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraResultLauncher.launch(intent);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                //open camera
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraResultLauncher.launch(intent);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void detectFaces(Bitmap bitmap) {
        binding.txtTitle.setText(new StringBuilder().append("Processing Image"));

        StringBuilder builder = new StringBuilder();

        InputImage inputImage = InputImage.fromBitmap(bitmap, 0);

        FaceDetectorOptions faceDetectorOptions = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                .enableTracking()
                .build();

        FaceDetector faceDetector = FaceDetection.getClient(faceDetectorOptions);

        Task<List<Face>> task = faceDetector.process(inputImage);
        task.addOnSuccessListener(new OnSuccessListener<List<Face>>() {
                    @Override
                    public void onSuccess(List<Face> faces) {
                        if (!faces.isEmpty()) {
                            builder.append(faces.size()).append(" Face Detected.\n\n");

                            for (Face face : faces) {
                                if (face != null) {
                                    int id = face.getTrackingId();

                                    float smilingProbability = face.getSmilingProbability();
                                    float leftEyeOpenProbability = face.getLeftEyeOpenProbability();
                                    float rightEyeOpenProbability = face.getRightEyeOpenProbability();

                                    float rotY = face.getHeadEulerAngleY();
                                    float rotZ = face.getHeadEulerAngleZ();
                                    float rotX = face.getHeadEulerAngleX();

                                    builder.append("1. Face Tracking ID = ").append(id).append("\n");
                                    builder.append("2. Head Rotation to Right = ").append(String.format("%.2f", rotY)).append("degrees").append("\n");
                                    builder.append("3. Smiling Probability = ").append(String.format("%.2f", smilingProbability * 100)).append("%").append("\n");
                                    builder.append("4. Left Eye Open Probability = ").append(String.format("%.2f", leftEyeOpenProbability * 100)).append("%").append("\n");
                                    builder.append("4. Right Eye Open Probability = ").append(String.format("%.2f", rightEyeOpenProbability * 100)).append("%").append("\n");
                                    builder.append("\n");
                                }
                            }

                            showDetection("Face Detection", builder, true);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Sorry! There is some error");
                    showDetection("Face Detection", stringBuilder, false);
                });
    }

    private void showDetection(String title, StringBuilder builder, boolean success) {
        if (success) {
            binding.txtTitle.setText(null);
            binding.txtTitle.setMovementMethod(new ScrollingMovementMethod());

            if (builder.length() > 0) {
                binding.txtTitle.append(builder);
                if (title.substring(0, title.indexOf(" ")).equalsIgnoreCase("OCR")) {
                    binding.txtTitle.append("\n(Hold the text to copy it!)");
                } else {
                    binding.txtTitle.append("(Hold the text to copy it!)");
                }
                binding.txtTitle.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText(title, builder);
                        clipboardManager.setPrimaryClip(clipData);
                        return true;
                    }
                });
            } else {
                binding.txtTitle.append(title.substring(0, title.indexOf(" ")));
                binding.txtTitle.append("Failed to find anything!");
            }
        } else {
            binding.txtTitle.setText(null);
            binding.txtTitle.setMovementMethod(new ScrollingMovementMethod());
            binding.txtTitle.append(builder);
        }
    }
}