package com.example.learnready.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skill_categories")
data class SkillCategory(
    @PrimaryKey
    val id: Int,
    val nameEn: String,
    val nameKn: String,
    val iconName: String,
    val color: String, // Hex color string
    val type: String   // digital_literacy, communication, analytical, technical
)
