package com.prince.contacts.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.prince.contacts.R
import com.prince.contacts.databinding.FragmentContactBinding
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactDao
import com.prince.contacts.models.ContactDatabase
import com.prince.contacts.models.ContactRepository
import com.prince.contacts.viewmodel.ContactViewModel
import com.prince.contacts.viewmodel.ContactViewModelFactory

class ContactFragment : Fragment(), ContactClickListener {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel
    private lateinit var binding: FragmentContactBinding
    private lateinit var adapter: ContactAdapter

    /*private val wordViewModel: ContactViewModel by viewModels {
        ContactViewModelFactory((getActivity().getApplicationContext() as ContactsApplication).repository)
    }*/


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_contact, container, false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val contactDao = ContactDatabase.getDatabase(requireContext()).ContactDao()
        val repository = ContactRepository(contactDao)
        val factory = ContactViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(ContactViewModel::class.java)
        // TODO: Use the ViewModel
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this

        // this creates a vertical layout Manager
        binding.recyclerview.layoutManager = LinearLayoutManager(context)

        viewModel.getNavigateToNewActivity()?.observe(viewLifecycleOwner, Observer {
            // Navigate to the new activity
            // Create an Intent to start the new activity
            val intent = Intent(activity, AddNewContactActivity::class.java)
            // Optionally, add extra data
            intent.putExtra("key", "value")
            // Start the new activity
            startActivity(intent)

        })

        binding.floatingActionButton.setOnClickListener(View.OnClickListener { // Call the ViewModel method to handle the button click
            viewModel.onPlusButtonClick()
        })

        /*// ArrayList of class ItemsViewModel
        val data = ArrayList<Contact>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..20) {
            data.add(Contact("8902975290", "Name $i " ,R.drawable.contactblack))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = ContactAdapter(data)*/

        initRecyclerView(contactDao)
        // Setting the Adapter with the recyclerview
        binding.recyclerview.adapter = adapter
    }

    private fun initRecyclerView(contactDao: ContactDao) {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())

        // ArrayList of class ItemsViewModel
        val data = ArrayList<Contact>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in 1..3) {
            //data.add(Contact(3 + i,"8902975290 $i", "Name $i " ,R.drawable.contactblack))
            //viewModel.insertContact(Contact("8902975290", "Name $i " ,R.drawable.contactblack))
        }

        adapter = ContactAdapter(data, this, contactDao)
        binding.recyclerview.adapter = adapter
        displayContactList()

    }

    private fun displayContactList() {
        viewModel.getAllContact().observe(viewLifecycleOwner, Observer {
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

}