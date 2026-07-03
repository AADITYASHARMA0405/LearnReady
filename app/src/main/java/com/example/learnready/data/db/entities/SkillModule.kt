package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "skill_modules",
    indices = [Index(value = ["categoryId"])],
    foreignKeys = [
        ForeignKey(
            entity = SkillCategory::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class SkillModule(
    @PrimaryKey
    val id: Int,
    val categoryId: Int,
    val titleEn: String,
    val titleKn: String,
    val descriptionEn: String,
    val descriptionKn: String,
    val contentEn: String = "",
    val contentKn: String = "",
    val keyPointsEn: String = "",
    val keyPointsKn: String = "",
    val orderIndex: Int
)
