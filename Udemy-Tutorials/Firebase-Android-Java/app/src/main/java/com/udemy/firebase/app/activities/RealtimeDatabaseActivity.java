package com.udemy.firebase.app.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udemy.firebase.app.R;
import com.udemy.firebase.app.databinding.ActivityMainBinding;
import com.udemy.firebase.app.models.User;

import java.util.Map;

public class RealtimeDatabaseActivity extends AppCompatActivity {

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

        getUsersFromRealtimeDatabase("users");

        addUserToRealtimeDatabase("users", new User("Priyanshi", "priyanshi@gmail.com"));
    }

    private void addUserToRealtimeDatabase(String referenceName, User user) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_db_url));
        DatabaseReference databaseReference = firebaseDatabase.getReference(referenceName);
        databaseReference.push().setValue(user);
    }

    private void getUsersFromRealtimeDatabase(String referenceName) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_db_url));
        DatabaseReference databaseReference = firebaseDatabase.getReference(referenceName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                Log.d("--users--", "onDataChange: " + map.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setValueToRealtimeDatabase(String referenceName, String message) {
        // initialise and access the firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_db_url));

        // get a reference to a specific node in the database
        DatabaseReference databaseReference = firebaseDatabase.getReference(referenceName);

        // write a value to the database to the specified location
        databaseReference.setValue(message);
    }

    private void getValueFromRealtimeDatabase(String referenceName) {
        // initialise and access the firebase database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(getString(R.string.firebase_realtime_db_url));

        // get a reference to a specific node in the database
        DatabaseReference databaseReference = firebaseDatabase.getReference(referenceName);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                binding.txtMessage.setText(value);

                Log.d("--data--", "onDataChange: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("--error--", "onCancelled: " + error.getDetails());
            }
        });
    }
}