package com.prince.contacts.models

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ProfileRepository(private val profileDao: ProfileDao) {

    val allProfiles: Flow<List<Profile>> =  profileDao.getAllProfiles()

    suspend fun insert(profile: Profile) {
        profileDao.insert(profile)
    }

    suspend fun update(profile: Profile) {
        profileDao.update(profile)
    }

    suspend fun delete(profileId: Long) {
        profileDao.delete(profileId)
    }
}
