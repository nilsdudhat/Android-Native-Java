package com.udemy.contactmanager.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.udemy.contactmanager.app.R;
import com.udemy.contactmanager.app.adapters.ContactAdapter;
import com.udemy.contactmanager.app.clicks.MainActivityClickHandlers;
import com.udemy.contactmanager.app.database.Contact;
import com.udemy.contactmanager.app.database.ContactViewModel;
import com.udemy.contactmanager.app.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ContactAdapter contactAdapter;

    ContactViewModel viewModel;

    List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding.setClick(new MainActivityClickHandlers(MainActivity.this));

        setupRecyclerView();

        viewModel = new ViewModelProvider(MainActivity.this).get(ContactViewModel.class);
        viewModel.getAllContacts().observe(MainActivity.this, contacts -> {
            contactList = new ArrayList<>(contacts);
            contactAdapter.setContactList(contacts);
        });
    }

    private void setupRecyclerView() {
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        contactAdapter = new ContactAdapter();
        binding.rvContacts.setAdapter(contactAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact = contactList.get(viewHolder.getAdapterPosition());
                viewModel.deleteContact(contact);
            }
        }).attachToRecyclerView(binding.rvContacts);
    }
}