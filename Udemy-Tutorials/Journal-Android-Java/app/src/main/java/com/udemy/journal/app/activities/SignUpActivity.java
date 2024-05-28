package com.udemy.journal.app.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udemy.journal.app.R;
import com.udemy.journal.app.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    // firebase authentication
    FirebaseAuth firebaseAuth;

    // firestore database
    FirebaseFirestore firestore;
    CollectionReference collectionReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initialiseFirebaseFunctions();

        binding.btnSignUp.setOnClickListener(v -> createUserAccount());
    }

    private void initialiseFirebaseFunctions() {
        firestore = FirebaseFirestore.getInstance();
        collectionReference = firestore.collection("Users");

        // Firebase Auth Initialisation
        firebaseAuth = FirebaseAuth.getInstance();

        // listening for changes in the authentication
        // state and responds accordingly when the state changes
        new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                // check the user if logged in or not
                if (user != null) {
                    // user already logged in
                } else {
                    // the user signed out
                }
            }
        };
    }

    private void createUserAccount() {
        String email = binding.edtEmail.getText().toString();
        String userName = binding.edtUserName.getText().toString();
        String password = binding.edtPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            binding.edtEmail.setError("Please enter Email");
            return;
        }
        if (TextUtils.isEmpty(userName)) {
            binding.edtUserName.setError("Please enter User Name");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            binding.edtPassword.setError("Please enter Password");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Account create - success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}