package com.udemy.contactmanager.app.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ContactRepository {

    private final ContactDao contactDao;

    ExecutorService executorService;
    public ContactRepository(Application application) {
        ContactDatabase contactDatabase = ContactDatabase.getInstance(application);

        this.contactDao = contactDatabase.getContactDao();

        // used for background database operation
        executorService = Executors.newSingleThreadExecutor();
    }

    public void insertContact(Contact contact) {
        // Runnable - to execute task on separate thread
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // database crud operation is not possible on main thread as default
                // it must be on background thread
                contactDao.insertContact(contact);
            }
        });
    }

    public void deleteContact(Contact contact) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                contactDao.deleteContact(contact);
            }
        });
    }

    public LiveData<List<Contact>> getAllContacts() {
        return contactDao.getAllContacts();
    }
}
