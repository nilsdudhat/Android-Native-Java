package com.udemy.journal.app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.udemy.journal.app.R;
import com.udemy.journal.app.adapters.JournalAdapter;
import com.udemy.journal.app.databinding.ActivityJournalsBinding;
import com.udemy.journal.app.models.Journal;

import java.util.ArrayList;
import java.util.List;

public class JournalsActivity extends AppCompatActivity {

    ActivityJournalsBinding binding;

    List<Journal> journalList = new ArrayList<>();

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    JournalAdapter journalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityJournalsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.toolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.action_add) {
                    if ((firebaseAuth != null) && (firebaseUser != null)) {
                        startActivity(new Intent(JournalsActivity.this, AddJournalActivity.class));
                    }
                } else if (id == R.id.action_sign_out) {
                    if ((firebaseAuth != null) && (firebaseUser != null)) {
                        signOut();
                    }
                }
                return true;
            }
        });

        setUpRecyclerView();

        initFirebase();

        getJournals();
    }

    private void getJournals() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference reference = firestore.collection("journals");
        reference.addSnapshotListener((value, error) -> {
            if (error != null) {
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                return;
            }
            if (value != null) {
                journalList.clear();

                List<DocumentSnapshot> list = value.getDocuments();
                for (DocumentSnapshot snapshot : list) {
                    Journal journal = snapshot.toObject(Journal.class);
                    journalList.add(journal);
                }

                setUpRecyclerView();
            }
        });
    }

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseAuth.addAuthStateListener(firebaseAuth -> {
            firebaseUser = firebaseAuth.getCurrentUser();

            if (firebaseUser == null) {
                startActivity(new Intent(JournalsActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void setUpRecyclerView() {
        if (binding.rvJournals.getLayoutManager() == null) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(JournalsActivity.this, LinearLayoutManager.VERTICAL, false);
            binding.rvJournals.setLayoutManager(layoutManager);
        }

        if (journalAdapter == null) {
            journalAdapter = new JournalAdapter(JournalsActivity.this);
            binding.rvJournals.setAdapter(journalAdapter);
        }
        journalAdapter.setJournalList(journalList);
    }

    private void signOut() {
        firebaseAuth.signOut();

        startActivity(new Intent(JournalsActivity.this, LoginActivity.class));
        finish();
    }
}