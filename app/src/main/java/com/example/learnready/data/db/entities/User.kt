package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey
    val id: String,
    val phone: String,
    val fullName: String?,
    val selectedLanguage: String = "en"
)
