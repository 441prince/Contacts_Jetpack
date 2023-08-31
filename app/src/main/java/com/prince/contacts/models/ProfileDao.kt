package com.prince.contacts.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profile_table ORDER BY profileName ASC")
    fun getAllProfiles(): Flow<List<Profile>>

    @Query("SELECT * FROM profile_table WHERE profileId = :profileId")
    suspend fun getProfileById(profileId: Long): Profile?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

    @Query("DELETE FROM profile_table WHERE profileId = :profileId")
    suspend fun delete(profileId: Long)
}
