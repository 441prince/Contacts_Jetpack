package com.prince.contacts.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.prince.contacts.R
import com.prince.contacts.databinding.ActivityAddNewContactBinding
import com.prince.contacts.models.ContactDatabase
import com.prince.contacts.models.ContactRepository
import com.prince.contacts.viewmodel.AddNewContactViewModel
import com.prince.contacts.viewmodel.AddNewContactViewModelFactory

class AddNewContactActivity : AppCompatActivity() {

    private lateinit var addNewContactViewModel: AddNewContactViewModel
    private lateinit var binding: ActivityAddNewContactBinding
    private val PICK_IMAGE = 1
    private val CAPTURE_IMAGE = 2
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_contact)
        val dao = ContactDatabase.getDatabase(application).ContactDao()
        val repository = ContactRepository(dao)
        val factory = AddNewContactViewModelFactory(application, repository)
        addNewContactViewModel =
            ViewModelProvider(this, factory).get(AddNewContactViewModel::class.java)
        binding.addNewContactViewModel = addNewContactViewModel
        binding.lifecycleOwner = this

        // Observe the selectedImageUri LiveData
        addNewContactViewModel.selectedImageUri.observe(this, Observer { uri ->
            uri?.let {
                // Display the selected image using an ImageView or load it using Glide
                Glide.with(this)
                    .load(uri)
                    .centerCrop() // Center-crop the image within the circular frame
                    .into(binding.selectContactImage)
            }
        })

        // Set a click listener for yourImageView to open the image picker
        binding.selectContactImage.setOnClickListener {
            checkPermissionAndPickImage()
        }
    }

    private fun checkPermissionAndPickImage() {
        val cameraPermission = android.Manifest.permission.CAMERA
        val storagePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE

        val cameraPermissionGranted = ContextCompat.checkSelfPermission(this, cameraPermission) == PackageManager.PERMISSION_GRANTED
        val storagePermissionGranted = ContextCompat.checkSelfPermission(this, storagePermission) == PackageManager.PERMISSION_GRANTED

        if (!cameraPermissionGranted || !storagePermissionGranted) {
            // Request permissions for both camera and storage if not granted
            val permissionsToRequest = mutableListOf<String>()
            if (!cameraPermissionGranted) {
                permissionsToRequest.add(cameraPermission)
            }
            if (!storagePermissionGranted) {
                permissionsToRequest.add(storagePermission)
            }
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), PERMISSION_CODE)
        } else {
            // Both permissions are granted
            openImagePicker()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE && grantResults.isNotEmpty()) {
            val allPermissionsGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allPermissionsGranted) {
                // All requested permissions are granted
                openImagePicker()
            } else {
                // Handle the case where not all permissions are granted
                // You can show a message to the user or take appropriate action
            }
        }
    }


    private fun openImagePicker() {
        val options = arrayOf("Camera", "Gallery", "Cancel")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose an option")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> addNewContactViewModel.captureImage(this)
                1 -> addNewContactViewModel.pickImageFromGallery(this)
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    companion object {
        private const val PERMISSION_CODE = 1001
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE -> {
                    if (data != null) {
                        val pickedImageUri = data.data
                        // Copy the picked image to the app's directory
                        if (pickedImageUri != null) {
                            addNewContactViewModel.copyPickedImageToAppDirectory(pickedImageUri)
                        }
                    }
                }
                CAPTURE_IMAGE -> {
                    // Image captured from camera, use the selectedImageUri from the ViewModel
                    addNewContactViewModel.selectedImageUri.value?.let { selectedImageUri ->
                        // You can use the selectedImageUri to display the captured image
                        // For example, with Glide or setImageURI on an ImageView
                        binding.selectContactImage.setImageURI(selectedImageUri)
                    }
                }
            }
        }
    }
}