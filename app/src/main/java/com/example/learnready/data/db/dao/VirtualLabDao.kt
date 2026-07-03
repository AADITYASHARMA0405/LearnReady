package com.example.learnready.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.learnready.data.db.entities.VirtualLab
import kotlinx.coroutines.flow.Flow

@Dao
interface VirtualLabDao {

    @Query("SELECT * FROM virtual_labs WHERE subjectId = :subjectId ORDER BY orderIndex")
    fun getLabsForSubject(subjectId: Int): Flow<List<VirtualLab>>

    @Query("SELECT * FROM virtual_labs ORDER BY orderIndex")
    fun getAllLabs(): Flow<List<VirtualLab>>

    @Query("SELECT * FROM virtual_labs WHERE id = :id")
    suspend fun getLabById(id: Int): VirtualLab?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(labs: List<VirtualLab>)
}
