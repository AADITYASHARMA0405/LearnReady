package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mock_interview_sessions",
    indices = [Index(value = ["userId"])],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class MockInterviewSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val startedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val overallScore: Float? = null,
    val communicationScore: Float? = null,
    val confidenceScore: Float? = null,
    val technicalScore: Float? = null,
    val responsesJson: String = "[]"  // JSON: [{questionId, answerText, commScore, confScore, techScore}]
)
