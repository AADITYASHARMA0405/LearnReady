package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "virtual_labs",
    indices = [Index(value = ["subjectId"])],
    foreignKeys = [
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class VirtualLab(
    @PrimaryKey
    val id: Int,
    val subjectId: Int,
    val titleEn: String,
    val titleKn: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val stepsJson: String,        // JSON array: [{"title":"...","instruction":"..."}]
    val estimatedMinutes: Int,
    val difficulty: String = "intermediate",
    val orderIndex: Int = 1
)
