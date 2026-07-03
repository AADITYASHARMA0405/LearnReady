package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.SurveyResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface SurveyResponseDao {

    @Query("SELECT * FROM survey_responses WHERE userId = :userId ORDER BY submittedAt DESC")
    fun getResponsesForUser(userId: String): Flow<List<SurveyResponse>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(response: SurveyResponse): Long
}
