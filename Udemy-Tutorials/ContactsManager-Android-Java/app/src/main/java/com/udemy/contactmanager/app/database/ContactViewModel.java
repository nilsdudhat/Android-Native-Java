package com.udemy.contactmanager.app.database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

/**
 * AndroidViewModel -
 * it is a subclass of ViewModel and similar to it,
 * both are designed to store and manage UI related data
 * and are responsible to prepare & provide data for UI
 * and automatically allow data to survive configuration changes.
 */
public class ContactViewModel extends AndroidViewModel {

    /*
     * If you need to use context inside ViewModel,
     * you have to use AndroidViewModel, because it provides access to the context
     */

    ContactRepository contactRepository;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        contactRepository = new ContactRepository(application);
    }

    public void insertContact(Contact contact) {
        contactRepository.insertContact(contact);
    }

    public void deleteContact(Contact contact) {
        contactRepository.deleteContact(contact);
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactRepository.getAllContacts();
    }
}
