package com.prince.contacts.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileRepository

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    fun getAllProfiles() = liveData {
        //insertContact(Contact(6, "123456", "Joel", R.drawable.filledheart))
        repository.allProfiles.collect {
            emit(it)
        }
    }

    suspend fun insert(profile: Profile) {
        repository.insert(profile)
    }

    suspend fun update(profile: Profile) {
        repository.update(profile)
    }

    suspend fun delete(profileId: Long) {
        repository.delete(profileId)
    }

}

class ProfileViewModelFactory(private val repository: ProfileRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}