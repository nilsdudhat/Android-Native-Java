package com.udemy.contactmanager.app.activities;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.udemy.contactmanager.app.R;
import com.udemy.contactmanager.app.clicks.AddNewContactActivityClickHandler;
import com.udemy.contactmanager.app.database.Contact;
import com.udemy.contactmanager.app.database.ContactViewModel;
import com.udemy.contactmanager.app.databinding.ActivityAddNewContactBinding;

public class AddNewContactActivity extends AppCompatActivity {

    ActivityAddNewContactBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = DataBindingUtil.setContentView(AddNewContactActivity.this, R.layout.activity_add_new_contact);
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Contact contact = new Contact();
        binding.setContact(contact);

        ContactViewModel viewModel = new ViewModelProvider(AddNewContactActivity.this).get(ContactViewModel.class);

        binding.setClick(new AddNewContactActivityClickHandler(AddNewContactActivity.this, contact, viewModel));
    }
}