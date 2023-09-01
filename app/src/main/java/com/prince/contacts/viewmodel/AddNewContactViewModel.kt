package com.prince.contacts.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteConstraintException
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactRepository
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date


class AddNewContactViewModel(
    private val application: Application,
    private val contactRepository: ContactRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputPhoneNumber = MutableLiveData<String>()
    val inputEmailId = MutableLiveData<String>()
    val navigateToAnotherActivity = MutableLiveData<Boolean>()
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    fun insertContact(contact: Contact) = viewModelScope.launch {
        try {
            _errorMessage.value = null // Clear any previous error message
            contactRepository.insert(contact)
        } catch (ex: SQLiteConstraintException) {
            // Handle the case of a duplicate phone number here
            _errorMessage.postValue("Phone number already exists.") // Set your error message
        }
    }

    fun clearErrorMessage() {
        _errorMessage.value = null
    }

    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun addContact() {
        if (inputName.value != null && inputPhoneNumber.value != null && inputEmailId.value != null) {

            viewModelScope.launch {
                val selectedProfile = profileRepository.getSelectedProfile()
                val currentProfileId = selectedProfile.id
                val contact = Contact(
                    id = 0,
                    phoneNumber = inputPhoneNumber.value!!,
                    name = inputName.value!!,
                    emailId = inputEmailId.value!!,
                    imageUri = selectedImageUri.value.toString(), // Convert Uri to String
                    isFavorite = false,
                    profileId = currentProfileId
                )
                insertContact(contact)
            }
            // Perform some actions, and then trigger navigation
            navigateToAnotherActivity.value = true
        }
    }

    fun pickImageFromGallery(activity: Activity) {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        val photoFile: File? = try {
            createImageFile()
        } catch (ex: IOException) {
            null
        }

        if (photoFile != null) {
            val photoUri = FileProvider.getUriForFile(
                activity,
                "${activity.packageName}.provider",
                photoFile
            )
            intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
            activity.startActivityForResult(intent, PICK_IMAGE)
        }
    }

    // Modify this function to copy the picked image to the app's external files directory
    fun copyPickedImageToAppDirectory(pickedImageUri: Uri) {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = application.getExternalFilesDir(null)

        try {
            if (storageDir != null) {
                val imageFile = File(storageDir, "JPEG_${timeStamp}.jpg")
                try {
                    val inputStream = application.contentResolver.openInputStream(pickedImageUri)
                    val outputStream = FileOutputStream(imageFile)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                    _selectedImageUri.value = Uri.fromFile(imageFile)
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            } else {
                throw IOException("External storage directory is null or not available.")
            }
        } catch (ex: IOException) {
            // Handle the exception here, you can log it or show an error message
            ex.printStackTrace()
        }
    }


    fun captureImage(activity: Activity) {
        val intent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(activity.packageManager) != null) {
            val photoFile: File? = try {
                createImageFile()
            } catch (ex: IOException) {
                null
            }
            if (photoFile != null) {
                val photoUri = FileProvider.getUriForFile(
                    activity,
                    "${activity.packageName}.provider",
                    photoFile
                )
                intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri)
                activity.startActivityForResult(intent, CAPTURE_IMAGE)
            }
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = application.getExternalFilesDir(null)

        if (storageDir != null) {
            val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
            _selectedImageUri.value = Uri.fromFile(imageFile)
            return imageFile
        } else {
            throw IOException("External storage directory is null or not available.")
        }
    }


    fun setImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

}

class AddNewContactViewModelFactory(
    private val application: Application,
    private val contactRepository: ContactRepository,
    private val profileRepository: ProfileRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewContactViewModel(application, contactRepository, profileRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}