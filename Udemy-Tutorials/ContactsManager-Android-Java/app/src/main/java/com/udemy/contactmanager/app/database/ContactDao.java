package com.udemy.contactmanager.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insertContact(Contact contact);

    @Delete
    void deleteContact(Contact contact);

    @Query("select * from contacts_table")
    LiveData<List<Contact>> getAllContacts();
}
