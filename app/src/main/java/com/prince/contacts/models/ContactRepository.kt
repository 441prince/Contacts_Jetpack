package com.prince.contacts.models

import androidx.annotation.WorkerThread

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

    val contacts = contactDao.getAllContact()
}