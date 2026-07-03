package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.SkillModule
import kotlinx.coroutines.flow.Flow

@Dao
interface SkillModuleDao {
    @Query("SELECT * FROM skill_modules WHERE categoryId = :categoryId ORDER BY orderIndex ASC")
    fun getModulesForCategory(categoryId: Int): Flow<List<SkillModule>>

    @Query("SELECT * FROM skill_modules WHERE id = :id")
    suspend fun getModuleById(id: Int): SkillModule?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(modules: List<SkillModule>)

    @Query("SELECT COUNT(*) FROM skill_modules WHERE categoryId = :categoryId")
    suspend fun getModuleCountForCategory(categoryId: Int): Int
}
