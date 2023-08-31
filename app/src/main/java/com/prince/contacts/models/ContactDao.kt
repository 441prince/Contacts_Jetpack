package com.prince.contacts.models

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact_table ORDER BY contactName ASC")
    fun getAllContact(): Flow<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE profileId = :profileId")
    fun getContactsForProfile(profileId: Long): LiveData<List<Contact>>

    @Query("SELECT * FROM contact_table WHERE contactNumber = :phoneNumber")
    suspend fun getContactByPhoneNumber(phoneNumber: String): Contact?

    @Query("SELECT * FROM contact_table WHERE isFavorite = 1")
    fun getFavoriteContacts(): LiveData<List<Contact>>

    @Insert
    suspend fun insertContact(contact: Contact)

    @Update
    suspend fun updateContact(contact: Contact)

    @Delete
    suspend fun deleteContact(contact: Contact)

    @Query("DELETE FROM contact_table WHERE contactId = :contactId")
    suspend fun deleteContactById(contactId: Long)

    @Query("DELETE FROM contact_table")
    suspend fun deleteAll()
}