package com.udemy.journal.app.activities;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.udemy.journal.app.R;
import com.udemy.journal.app.databinding.ActivityAddJournalBinding;
import com.udemy.journal.app.models.Journal;

import java.util.Date;

public class AddJournalActivity extends AppCompatActivity {

    ActivityAddJournalBinding binding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    Uri imageUri;

    ActivityResultLauncher<String> cameraResultLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    imageUri = uri;
                    binding.imgJournal.setImageURI(uri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddJournalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();

        initFirebase();
    }

    private void saveJournal() {

        if (imageUri == null) {
            Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
        }

        String title = binding.edtTitle.getText().toString().trim();
        String description = binding.edtDesc.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            binding.edtTitle.setError("Please enter Title");
            return;
        }
        if (TextUtils.isEmpty(description)) {
            binding.edtDesc.setError("Please enter Description");
            return;
        }

        binding.progressBar.setVisibility(View.VISIBLE);

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference fileReference = storageReference.child("journal_images")
                .child(String.valueOf(Timestamp.now().getSeconds()));

        fileReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileReference.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();

                                        Journal journal = getJournal(imageUrl, description, title);

                                        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                        firebaseFirestore.collection("journals")
                                                .document()
                                                .set(journal)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        binding.progressBar.setVisibility(View.GONE);
                                                        finish();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        binding.progressBar.setVisibility(View.GONE);
                                                        Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        binding.progressBar.setVisibility(View.GONE);

                                        Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.progressBar.setVisibility(View.GONE);

                        Toast.makeText(AddJournalActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @NonNull
    private Journal getJournal(String imageUrl, String description, String title) {
        Journal journal = new Journal();
        journal.setThoughts(description);
        journal.setTitle(title);
        journal.setImageUrl(imageUrl);
        journal.setTimeAdded(new Timestamp(new Date()));
        journal.setUserName(firebaseUser.getDisplayName());
        journal.setUserID(firebaseUser.getUid());
        return journal;
    }

    private void initViews() {
        binding.progressBar.setVisibility(View.GONE);

        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // getting image from the library
                cameraResultLauncher.launch("image/*");
            }
        });

        binding.btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJournal();
            }
        });
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseAuth.addAuthStateListener(firebaseAuth -> firebaseUser = firebaseAuth.getCurrentUser());
    }
}