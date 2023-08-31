package com.prince.contacts.view

import com.prince.contacts.models.Contact
import com.prince.contacts.models.Profile

interface ItemClickListener {
    fun onContactClick(contact: Contact)
    fun onProfileClick(profile: Profile)
}
