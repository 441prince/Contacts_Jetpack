package com.prince.contacts.view

import com.prince.contacts.models.Contact

interface ContactClickListener {
    fun onContactClick(contact: Contact)
}
