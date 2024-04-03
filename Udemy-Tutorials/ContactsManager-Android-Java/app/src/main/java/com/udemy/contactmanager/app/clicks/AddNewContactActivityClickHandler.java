package com.udemy.contactmanager.app.clicks;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.udemy.contactmanager.app.database.Contact;
import com.udemy.contactmanager.app.database.ContactViewModel;

public class AddNewContactActivityClickHandler {

    Activity activity;
    Contact contact;
    ContactViewModel viewModel;

    public AddNewContactActivityClickHandler(Activity activity, Contact contact, ContactViewModel viewModel) {
        this.activity = activity;
        this.contact = contact;
        this.viewModel = viewModel;
    }

    public void onSaveButtonClick(View view) {
        if ((contact.getName() == null) || (contact.getEmail() == null)) {
            Toast.makeText(activity, "Name of Email cannot be empty", Toast.LENGTH_SHORT).show();
        } else {
            viewModel.insertContact(contact);
            activity.finish();
        }
    }
}
