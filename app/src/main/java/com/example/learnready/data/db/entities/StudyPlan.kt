package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "study_plan",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["moduleId"]),
        Index(value = ["lessonId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Module::class,
            parentColumns = ["id"],
            childColumns = ["moduleId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class StudyPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val moduleId: Int,
    val lessonId: Int? = null,
    val type: String, // "lesson" or "quiz"
    val titleEn: String,
    val titleKn: String = "",
    val reasonLabel: String, // "Weak area", "Next in sequence", "Review needed"
    val recommendedOrder: Int,
    val dueDate: Long? = null,
    val status: String = "pending" // "pending", "completed"
)
