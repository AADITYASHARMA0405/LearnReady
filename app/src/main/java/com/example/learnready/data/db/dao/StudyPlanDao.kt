package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.StudyPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyPlanDao {
    @Query("SELECT * FROM study_plan WHERE userId = :userId AND status = 'pending' ORDER BY recommendedOrder ASC LIMIT 4")
    fun getPendingPlan(userId: String): Flow<List<StudyPlan>>

    @Query("SELECT * FROM study_plan WHERE userId = :userId ORDER BY recommendedOrder ASC")
    fun getAllPlans(userId: String): Flow<List<StudyPlan>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlan(plan: StudyPlan)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plans: List<StudyPlan>)

    @Query("UPDATE study_plan SET status = 'completed' WHERE id = :planId")
    suspend fun markCompleted(planId: Int)

    @Query("DELETE FROM study_plan WHERE userId = :userId AND status = 'pending'")
    suspend fun clearPendingPlans(userId: String)

    @Query("SELECT COUNT(*) FROM study_plan WHERE userId = :userId AND status = 'completed'")
    suspend fun getCompletedCount(userId: String): Int
}
