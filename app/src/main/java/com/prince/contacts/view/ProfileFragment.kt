package com.prince.contacts.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.prince.contacts.R
import com.prince.contacts.databinding.FragmentProfileBinding
import com.prince.contacts.models.AppDatabase
import com.prince.contacts.models.Contact
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileDao
import com.prince.contacts.models.ProfileRepository
import com.prince.contacts.viewmodel.ProfileViewModel
import com.prince.contacts.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    private lateinit var viewModel: ProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_profile, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val profileDao = AppDatabase.getDatabase(requireContext()).ProfileDao()
        val repository = ProfileRepository(profileDao)
        val factory = ProfileViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        // Define your default profile
        val defaultProfile = Profile(id = 1, name = "Default Profile", isDefault = true, imageUri = null)
        viewModel.addDefaultProfile(defaultProfile)

        // this creates a vertical layout Manager
        binding.profileRecyclerView.layoutManager = GridLayoutManager(context, 2)
        initRecyclerView(profileDao)
        displayContactList()

        viewModel.getNavigateToNewActivity()?.observe(viewLifecycleOwner, Observer {
            // Navigate to the new activity
            // Create an Intent to start the new activity
            val intent = Intent(activity, AddViewEditProfileActivity::class.java)
            // Optionally, add extra data
            intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)

        })

        binding.floatingActionButton.setOnClickListener(View.OnClickListener { // Call the ViewModel method to handle the button click
            viewModel.onPlusButtonClick()
        })
    }

    private fun initRecyclerView(profileDao: ProfileDao) {
        binding.profileRecyclerView.layoutManager = GridLayoutManager(context, 2)
        adapter = ProfileAdapter(requireContext(), ArrayList(), this, profileDao, viewModel)
        binding.profileRecyclerView.adapter = adapter
    }

    private fun displayContactList() {
        viewModel.getAllProfiles().observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onContactClick(contact: Contact) {

    }

    override fun onProfileClick(profile: Profile) {

        // Create an Intent to open the EditContactActivity
        val intent = Intent(requireContext(), AddViewEditProfileActivity::class.java)

        // Pass the contact data to the EditContactActivity
        intent.putExtra("profile_id", profile.id)

        // Start the EditContactActivity
        startActivity(intent)
    }

}