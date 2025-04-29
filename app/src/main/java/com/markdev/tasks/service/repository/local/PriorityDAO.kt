package com.markdev.tasks.service.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.markdev.tasks.service.model.PriorityModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PriorityDAO {

    @Insert
    suspend fun save(list: List<PriorityModel>)

    @Query("SELECT * FROM Priority")
    fun list(): Flow<List<PriorityModel>>

    @Query("SELECT description FROM Priority WHERE id = :id")
    suspend fun getDescription(id: Int): String

    @Query("DELETE FROM Priority")
    suspend fun clear()
}