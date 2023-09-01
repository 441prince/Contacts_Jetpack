package com.prince.contacts.view

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager.widget.ViewPager
import com.prince.contacts.R
import com.prince.contacts.ViewPagerAdapter
import com.prince.contacts.databinding.FragmentFavoriteBinding
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactDao
import com.prince.contacts.models.AppDatabase
import com.prince.contacts.models.ContactRepository
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileRepository
import com.prince.contacts.viewmodel.FavoriteViewModel
import com.prince.contacts.viewmodel.FavoriteViewModelFactory

class FavoriteFragment : Fragment(), ItemClickListener {

    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var adapter: FavoriteAdapter
    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_favorite, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewPager = requireActivity().findViewById(R.id.viewPager) // Replace with your ViewPager ID
        viewPagerAdapter = viewPager.adapter as ViewPagerAdapter

        val contactDao = AppDatabase.getDatabase(requireContext()).ContactDao()
        val repository = ContactRepository(contactDao)
        val profileDao = AppDatabase.getDatabase(requireContext()).ProfileDao()
        val profileRepository = ProfileRepository(profileDao)
        val factory = FavoriteViewModelFactory(repository, profileRepository)
        viewModel = ViewModelProvider(this, factory).get(FavoriteViewModel::class.java)
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        // this creates a vertical layout Manager
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(context, 2)
        initRecyclerView(contactDao)
        displayContactList()
    }

    private fun initRecyclerView(contactDao: ContactDao) {
        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(context, 2)

        /*// ArrayList of class ItemsViewModel
        val data = ArrayList<Contact>()

        adapter = FavoriteAdapter(data, this, contactDao)
        binding.favoriteRecyclerView.adapter = adapter*/

        adapter = FavoriteAdapter(requireContext(), viewPager, ArrayList(), this, contactDao, viewModel)
        binding.favoriteRecyclerView.adapter = adapter


    }

    private fun displayContactList() {
        viewModel.favoriteContacts.observe(viewLifecycleOwner, Observer {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    override fun onContactClick(contact: Contact) {

        // Create an Intent to open the EditContactActivity
        val intent = Intent(requireContext(), ViewOrEditContactActivity::class.java)

        // Pass the contact data to the EditContactActivity
        intent.putExtra("contact_phone", contact.phoneNumber)

        // Start the EditContactActivity
        startActivity(intent)
    }

    override fun onProfileClick(profile: Profile) {

    }

    override fun onProfileLongClick(profile: Profile) {

    }

    override fun onResume() {
        super.onResume()
        //Toast.makeText(requireContext(), "Im FavoriteFragment", Toast.LENGTH_SHORT).show()
    }
}