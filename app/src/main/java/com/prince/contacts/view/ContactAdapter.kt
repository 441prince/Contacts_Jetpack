package com.prince.contacts.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.prince.contacts.R
import com.prince.contacts.models.Contact

class ContactAdapter(private val mList: List<Contact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {

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

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.contactImageView.setImageResource(ItemsViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.contactNameTextView.text = ItemsViewModel.name

        // sets the text to the textview from our itemHolder class
        holder.contactNumberTextView.text = ItemsViewModel.number.toString()

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val contactImageView: ImageView = itemView.findViewById(R.id.contact_imageview)
        val contactNameTextView: TextView = itemView.findViewById(R.id.contact_name_textView)
        val contactNumberTextView: TextView = itemView.findViewById(R.id.contact_number_textView)
    }
}