package com.example.businesscard.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactsDao {
    @Query("SELECT * FROM contacts_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<Contacts>>

    @Query("SELECT * FROM contacts_table WHERE id = :id")
    fun getById(id: Long): Contacts?

    @Insert
    fun insert(contacts: Contacts?)

    @Update
    fun update(contacts: Contacts?)

    @Delete
    fun delete(contacts: Contacts?)

    @Query("DELETE FROM contacts_table")
    suspend fun deleteAllContacts()
}