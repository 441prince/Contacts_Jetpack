package com.prince.contacts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prince.contacts.R
import com.prince.contacts.models.Contact
import com.prince.contacts.models.ContactDao
import com.prince.contacts.models.ContactRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContactAdapter(
    private val contactsList: ArrayList<Contact>,
    private val clickListener: ContactClickListener,
    private val contactDao: ContactDao
) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_list_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = contactsList[position]

        // sets the image to the imageview from our itemHolder class
        // Load the image using Glide
        Glide.with(holder.itemView.context)
            .load(ItemsViewModel.imageUri) // Assuming contact.imageUri is a String
            .centerCrop() // Center-crop the image within the circular frame
            .into(holder.contactImageView)

        // sets the text to the textview from our itemHolder class
        holder.contactNameTextView.text = ItemsViewModel.name

        // sets the text to the textview from our itemHolder class
        holder.contactNumberTextView.text = ItemsViewModel.phoneNumber.toString()

        // Set the favorite image based on the isFavorite property
        val favoriteImageResource = if (ItemsViewModel.isFavorite) {
            R.drawable.filledheart // Use your favorite image resource
        } else {
            R.drawable.emptyheart // Use your non-favorite image resource
        }
        holder.contactFavoriteImageView.setImageResource(favoriteImageResource)

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return contactsList.size
    }

    fun setList(contacts: List<Contact>) {
        contactsList.clear()
        contactsList.addAll(contacts)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    // Holds the views for adding it to image and text
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contactImageView: ImageView = itemView.findViewById(R.id.contact_imageview)
        val contactNameTextView: TextView = itemView.findViewById(R.id.contact_name_textView)
        val contactNumberTextView: TextView = itemView.findViewById(R.id.contact_number_textView)
        val contactFavoriteImageView: ImageButton =
            itemView.findViewById(R.id.contact_favorite_imageview)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactsList[position]
                    clickListener.onContactClick(contact)
                }
            }
            contactFavoriteImageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = contactsList[position]
                    contact.isFavorite = !contact.isFavorite // Toggle the favorite state

                    // Use a coroutine scope to call the suspend function
                    CoroutineScope(Dispatchers.IO).launch {
                        // Update the contact in the Room database
                        val repository = ContactRepository(contactDao) // Use your ContactRepository
                        repository.update(contact)
                    }

                    notifyDataSetChanged() // Refresh the list to update the ImageButton
                }
            }
        }
    }
}