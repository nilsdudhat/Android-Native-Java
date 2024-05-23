package com.udemy.firebase.app.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.udemy.firebase.app.R;
import com.udemy.firebase.app.databinding.ActivityFirestoreDatabaseBinding;
import com.udemy.firebase.app.models.Friend;

public class FirestoreDatabaseActivity extends AppCompatActivity {

    ActivityFirestoreDatabaseBinding binding;

    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = firestore.collection("Users");
    DocumentReference documentReference = collectionReference.document("i8fW970W2sfRnczembEO");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityFirestoreDatabaseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFriend();
            }
        });

        binding.btnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllDocumentsFromCollection();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateSpecificDocument();
            }
        });

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDocument();
            }
        });
    }

    private void updateSpecificDocument() {
        if (binding.edtName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.edtName.getText().toString();
        String email = binding.edtEmail.getText().toString();

        documentReference.update("name", name);
        documentReference.update("email", email);
    }

    private void deleteDocument() {
        documentReference.delete();
    }

    private void getAllDocumentsFromCollection() {
        collectionReference.get().addOnSuccessListener(queryDocumentSnapshots -> {
            // this callback is executed when data retrieval is successful
            // the queryDocumentSnapshots contains the documents in the collection
            // each queryDocumentSnapshot represents document in the collection

            StringBuilder data = new StringBuilder();

            for (QueryDocumentSnapshot queryDocumentSnapshot: queryDocumentSnapshots) {

                // transforming snapshots into objects
                Friend friend = queryDocumentSnapshot.toObject(Friend.class);

                data.append("Name: ").append(friend.getName())
                        .append(" & ")
                        .append("Email: ").append(friend.getEmail())
                        .append("\n");
            }
            binding.txtOutput.setText(data.toString());
        });
    }

    private void saveFriend() {
        if (binding.edtName.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = binding.edtName.getText().toString();
        String email = binding.edtEmail.getText().toString();

        Friend friend = new Friend(name, email);

        collectionReference.add(friend).addOnSuccessListener(documentReference -> {
            String docId = documentReference.getId();
            String docPath = documentReference.getPath();

            Log.d("--friend--", docId + ": " + docPath);
        });
    }
}