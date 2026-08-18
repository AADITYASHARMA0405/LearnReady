package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "interview_questions")
data class InterviewQuestion(
    @PrimaryKey
    val id: Int,
    val categoryTag: String,   // "communication", "technical", "behavioral"
    val questionEn: String,
    val questionKn: String,
    val difficulty: String = "intermediate",
    val rubricHintsEn: String = "",  // Guidance text for self-rating
    val orderIndex: Int = 0
)
