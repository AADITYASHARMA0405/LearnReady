package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.SkillCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillCategoryDao {
    @Query("SELECT * FROM skill_categories ORDER BY id ASC")
    fun getAllCategories(): Flow<List<SkillCategory>>

    @Query("SELECT * FROM skill_categories WHERE id = :id")
    suspend fun getCategoryById(id: Int): SkillCategory?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<SkillCategory>)
}
