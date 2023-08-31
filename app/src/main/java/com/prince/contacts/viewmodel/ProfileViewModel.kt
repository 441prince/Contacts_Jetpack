package com.prince.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // Define a LiveData to trigger navigation
    private val navigateToNewActivity = MutableLiveData<Boolean>()

    fun getAllProfiles() = liveData {
        //insertContact(Contact(6, "123456", "Joel", R.drawable.filledheart))
        repository.allProfiles.collect {
            emit(it)
        }
    }

    fun addDefaultProfile(defaultProfile: Profile) = viewModelScope.launch{
        // Ensure a default profile is available in the database
        repository.checkAndInsertDefaultProfile(defaultProfile)
    }

    suspend fun insert(profile: Profile) {
        repository.insert(profile)
    }

    suspend fun update(profile: Profile) {
        repository.update(profile)
    }

    suspend fun delete(profileId: Long) {
        repository.deleteProfileById(profileId)
    }

    fun getNavigateToNewActivity(): LiveData<Boolean>? {
        return navigateToNewActivity
    }
    fun onPlusButtonClick() {
        navigateToNewActivity.value = true
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