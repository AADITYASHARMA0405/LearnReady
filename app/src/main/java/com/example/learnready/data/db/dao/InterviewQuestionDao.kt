package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.InterviewQuestion
import kotlinx.coroutines.flow.Flow

@Dao
interface InterviewQuestionDao {

    @Query("SELECT * FROM interview_questions ORDER BY orderIndex")
    fun getAllQuestions(): Flow<List<InterviewQuestion>>

    @Query("SELECT * FROM interview_questions WHERE categoryTag = :category ORDER BY orderIndex")
    fun getQuestionsByCategory(category: String): Flow<List<InterviewQuestion>>

    @Query("SELECT * FROM interview_questions ORDER BY RANDOM() LIMIT :count")
    suspend fun getRandomQuestions(count: Int): List<InterviewQuestion>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(questions: List<InterviewQuestion>)
}
