package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "lessons",
    foreignKeys = [
        ForeignKey(
            entity = Module::class,
            parentColumns = ["id"],
            childColumns = ["moduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Lesson(
    @PrimaryKey
    val id: Int,
    val moduleId: Int,
    val titleEn: String,
    val titleKn: String,
    val contentEn: String,
    val contentKn: String,
    val keyPointsEn: String,
    val keyPointsKn: String,
    val orderIndex: Int
)
