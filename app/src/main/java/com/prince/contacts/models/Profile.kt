package com.prince.contacts.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class Profile(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "profileId")
    val id: Long,

    @ColumnInfo(name = "profileName")
    val name: String,

    @ColumnInfo var imageUri: String?,

    @ColumnInfo(name = "isDefault")
    val isDefault: Boolean = false
)

