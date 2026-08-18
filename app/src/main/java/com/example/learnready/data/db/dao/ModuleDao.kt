package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.Module
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleDao {
    @Query("SELECT * FROM modules WHERE subjectId = :subjectId ORDER BY orderIndex ASC")
    fun getModulesForSubject(subjectId: Int): Flow<List<Module>>

    @Query("SELECT * FROM modules WHERE id = :id")
    suspend fun getModuleById(id: Int): Module?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(modules: List<Module>)
}
