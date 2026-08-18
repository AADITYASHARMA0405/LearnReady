package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey
    val id: Int,
    val nameEn: String,
    val nameKn: String,
    val stream: String,
    val iconName: String,
    val color: String
)
