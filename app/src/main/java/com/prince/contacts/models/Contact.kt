package com.prince.contacts.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
    (tableName = "contact_table")
data class Contact(
    @PrimaryKey @ColumnInfo(name = "contactNumber") val phoneNumber: String,
    @ColumnInfo(name = "contactName") val name: String,
    @ColumnInfo(name = "contactEmailId") val emailId: String,
    @ColumnInfo val imageUri: String,
    @ColumnInfo(name = "isFavorite") var isFavorite: Boolean
) {
}
