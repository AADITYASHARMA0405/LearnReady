package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.StudentProgress
import kotlinx.coroutines.flow.Flow

@Dao
interface ProgressDao {
    @Query("SELECT * FROM student_progress WHERE userId = :userId AND moduleId = :moduleId")
    fun getProgressForModule(userId: String, moduleId: Int): Flow<List<StudentProgress>>

    @Query("SELECT * FROM student_progress WHERE userId = :userId")
    fun getAllProgress(userId: String): Flow<List<StudentProgress>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProgress(progress: StudentProgress)
}
