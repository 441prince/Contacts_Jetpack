package com.prince.contacts.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prince.contacts.R
import com.prince.contacts.viewmodel.ContactViewModel

class ContactFragment : Fragment() {

    companion object {
        fun newInstance() = ContactFragment()
    }

    private lateinit var viewModel: ContactViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
        // TODO: Use the ViewModel
    }

}