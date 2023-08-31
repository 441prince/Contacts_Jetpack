package com.prince.contacts.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.prince.contacts.R
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoriteViewModel(private val repository: ContactRepository) : ViewModel() {
    // TODO: Implement the ViewModel


    val favoriteContacts: LiveData<List<Contact>> = repository.getFavoriteContacts()

    // Define a LiveData to trigger navigation
    private val navigateToNewActivity = MutableLiveData<Boolean>()
    private val favImageButton = MutableLiveData<Int>()
    fun updateContactAndNotify(contact: Contact) {
        viewModelScope.launch {
            // Update the contact in the Room database
            repository.update(contact)

            /*// Notify the LiveData on the main thread
            // Notify the LiveData on the main thread
            withContext(Dispatchers.Main) {
                notifyRepositoryEvent(
                    ContactRepository.RepositoryEvent(
                        ContactRepository.RepositoryEvent.Action.NOTIFY_DATA_SET_CHANGED
                    )
                )
            }*/
        }
    }

    fun getNavigateToNewActivity(): LiveData<Boolean>? {
        return navigateToNewActivity
    }

    // Method to handle button click and trigger navigation
    fun onPlusButtonClick() {
        navigateToNewActivity.value = true
    }

    fun onFavoriteButtonClick() {
        if (favImageButton.value == R.drawable.emptyheart) {
            favImageButton.value = R.drawable.filledheart
        } else {
            favImageButton.value = R.drawable.emptyheart
        }
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */

    fun getAllFavContact() = liveData {
        //insertContact(Contact(6, "123456", "Joel", R.drawable.filledheart))
        repository.contacts.collect {
            emit(it)
        }
    }

    fun insertContact(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }
}


class FavoriteViewModelFactory(private val repository: ContactRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}