package com.prince.contacts.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactRepository
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date

class AddNewContactViewModel (private val application: Application, private val repository: ContactRepository) : ViewModel() {

    val inputName = MutableLiveData<String>()
    val inputPhoneNumber = MutableLiveData<String>()
    val inputEmailId = MutableLiveData<String>()

    private val _selectedImageUri = MutableLiveData<Uri?>()
    val selectedImageUri: LiveData<Uri?> = _selectedImageUri

    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    fun insertContact(contact: Contact) = viewModelScope.launch {
        repository.insert(contact)
    }

    fun addContact() {
        if (inputName.value != null && inputPhoneNumber.value != null && inputEmailId.value != null) {
            val contact = Contact(
                phoneNumber = inputPhoneNumber.value!!,
                name = inputName.value!!,
                emailId = inputEmailId.value!!,
                imageUri = selectedImageUri.value.toString(), // Convert Uri to String
                isFavorite = false
            )

            insertContact(contact)
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

class AddNewContactViewModelFactory (private val application: Application, private val repository: ContactRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewContactViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewContactViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}