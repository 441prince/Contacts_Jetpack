package com.prince.contacts.models

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ContactRepository(private val contactDao: ContactDao) {

    /*// Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allWords: Flow<List<Word>> = contactDao.getAlphabetizedWords()*/

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(contact: Contact) {
        contactDao.insertContact(contact)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun update(contact: Contact) {
        contactDao.updateContact(contact)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteContactById(contactId: Long) {
        contactDao.deleteContactById(contactId)
    }

    // Function to get a contact by phone number
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact? {
        return contactDao.getContactByPhoneNumber(phoneNumber)
    }

    fun getFavoriteContacts(): LiveData<List<Contact>> {
        return contactDao.getFavoriteContacts()
    }

    fun getContactsForProfile(profileId: Long): LiveData<List<Contact>> {
        return contactDao.getContactsForProfile(profileId)
    }

    val contacts: Flow<List<Contact>> = contactDao.getAllContact()
}