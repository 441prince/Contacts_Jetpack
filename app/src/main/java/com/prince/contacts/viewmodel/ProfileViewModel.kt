package com.prince.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.viewpager.widget.ViewPager
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {

    // Define a LiveData to trigger navigation
    private val navigateToNewActivity = MutableLiveData<Boolean>()
    private val _selectedProfile = MutableLiveData<Profile?>()
    val selectedProfile: LiveData<Profile?> get() = _selectedProfile

    fun getAllProfiles() = liveData {
        //insertContact(Contact(6, "123456", "Joel", R.drawable.filledheart))
        repository.allProfiles.collect {
            emit(it)
        }
    }

    // Method to select a profile
    fun selectProfile(profileId: Long, viewPager: ViewPager) {
        viewModelScope.launch {
            /*// Deselect the currently selected profile (if any)
            _selectedProfile.value?.let {
                it.isSelected = false
                repository.update(it)
            }

            // Select the new profile
            val profile = repository.getProfileById(profileId)
            profile?.isSelected = true
            _selectedProfile.value = profile

            // Update the database
            repository.update(profile!!)*/

            repository.selectProfile(profileId)
            viewPager.adapter?.notifyDataSetChanged()
        }
    }

    fun addDefaultProfile(defaultProfile: Profile) = viewModelScope.launch {
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

class ProfileViewModelFactory(private val repository: ProfileRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}