package com.example.businesscard.data

import androidx.lifecycle.LiveData

class ContactsRepository(private var wordsDao: ContactsDao) {

    val readAllData: LiveData<List<Contacts>> = wordsDao.readAllData()

    suspend fun addContact(contact: Contacts){
        wordsDao.insert(contact)
    }
    suspend fun deleteAllContacts(){
        wordsDao.deleteAllContacts()
    }
    suspend fun delete(words: Contacts){
        wordsDao.delete(words)
    }
    suspend fun update(contacts: Contacts){
        wordsDao.update(contacts)
    }
}