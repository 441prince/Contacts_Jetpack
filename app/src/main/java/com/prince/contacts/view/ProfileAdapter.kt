package com.prince.contacts.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.prince.contacts.R
import com.prince.contacts.models.Profile
import com.prince.contacts.models.ProfileDao
import com.prince.contacts.viewmodel.ProfileViewModel

class ProfileAdapter(
    val context: Context,
    private val profileList: ArrayList<Profile>,
    private val clickListener: ItemClickListener,
    private val profileDao: ProfileDao,
    private val viewModel: ProfileViewModel // Add ViewModel parameter
) : RecyclerView.Adapter<ProfileAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.profile_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val profile = profileList[position]
        // sets the image to the imageview from our itemHolder class
        // Load the image using Glide
       /* Glide.with(holder.itemView.context)
            .load(profile.imageUri)
            .centerCrop()
            .into(holder.profileImageView)*/

        Glide.with(holder.itemView.context)
            .load(profile.imageUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.contactblack) // Set your default image resource here
                    .error(R.drawable.contactblack) // Set your default image resource here as well
                    .centerCrop()
            )
            .into(holder.profileImageView)

        // sets the text to the textview from our itemHolder class
        holder.profileNameTextView.text = profile.name
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    fun setList(profiles: List<Profile>) {
        profileList.clear()
        profileList.addAll(profiles)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profile_imageview)
        val profileNameTextView: TextView = itemView.findViewById(R.id.profile_name_textView)
        // Add other views here as needed


        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val profile = profileList[position]
                    clickListener.onProfileClick(profile)
                }
            }
        }
    }
}
            /*contactFavoriteImageView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val contact = profileList[position]
                    //contact.isFavorite = !contact.isFavorite // Toggle the favorite state

                    *//*//*/ Use a coroutine scope to call the suspend function
                    CoroutineScope(Dispatchers.IO).launch {
                        // Update the contact in the Room database
                        val repository = ContactRepository(contactDao) // Use your ContactRepository
                        repository.update(contact)


                        // Notify the ViewModel which will, in turn, notify the LiveData
                        viewModel.notifyRepositoryEvent(
                            ContactRepository.RepositoryEvent(
                                ContactRepository.RepositoryEvent.Action.NOTIFY_DATA_SET_CHANGED
                            )
                        )
                    }

                    notifyDataSetChanged() // Refresh the list to update the ImageButton*//*

                    // Use ViewModel to update the contact and notify LiveData
                    //viewModel.updateContactAndNotify(contact)
                    Toast.makeText(
                        context,
                        "FavoriteAdapter position $position",
                        Toast.LENGTH_SHORT
                    ).show();

                    // No need to call notifyDataSetChanged() here
                }
            }*/