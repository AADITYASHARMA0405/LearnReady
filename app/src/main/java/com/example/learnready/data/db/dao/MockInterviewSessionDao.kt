package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.learnready.data.db.entities.MockInterviewSession
import kotlinx.coroutines.flow.Flow

@Dao
interface MockInterviewSessionDao {

    @Query("SELECT * FROM mock_interview_sessions WHERE userId = :userId ORDER BY startedAt DESC")
    fun getSessionsForUser(userId: String): Flow<List<MockInterviewSession>>

    @Query("SELECT * FROM mock_interview_sessions WHERE id = :id")
    suspend fun getSessionById(id: Int): MockInterviewSession?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(session: MockInterviewSession): Long

    @Update
    suspend fun update(session: MockInterviewSession)
}
