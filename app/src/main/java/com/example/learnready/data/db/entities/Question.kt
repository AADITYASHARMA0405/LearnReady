package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "questions",
    foreignKeys = [
        ForeignKey(
            entity = Module::class,
            parentColumns = ["id"],
            childColumns = ["moduleId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Subject::class,
            parentColumns = ["id"],
            childColumns = ["subjectId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Question(
    @PrimaryKey
    val id: Int,
    val moduleId: Int,
    val subjectId: Int,
    val type: String,
    val questionEn: String,
    val questionKn: String,
    val optionsEn: String, // Stored as JSON string
    val optionsKn: String, // Stored as JSON string
    val correctAnswer: String,
    val explanationEn: String,
    val explanationKn: String,
    val difficulty: Int
)
