package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "survey_responses",
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
data class SurveyResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String,
    val surveyType: String, // "pre_training", "post_training", "tam"
    val responsesJson: String, // JSON: Map<String, Int> for question key -> likert value (1-5)
    val submittedAt: Long = System.currentTimeMillis()
)
