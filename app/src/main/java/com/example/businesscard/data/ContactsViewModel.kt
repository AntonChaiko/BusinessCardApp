package com.example.businesscard.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContactsViewModel(application: Application) : AndroidViewModel(application) {
    val readAllData: LiveData<List<Contacts>>
    private val repository: ContactsRepository

    init {
        val contactsDao = ContactsDatabase.getDatabase(application).contactsDao()
        repository = ContactsRepository(contactsDao!!)
        readAllData = repository.readAllData
    }

    fun addContacts(contacts: Contacts) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addContact(contacts)
        }
    }

    fun deleteAllWords() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllContacts()
        }
    }
    fun deleteCurrentWord(words: Contacts) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.delete(words)
        }
    }
    fun update(contact: Contacts){
        viewModelScope.launch(Dispatchers.IO) {
            repository.update(contact)
        }
    }
}