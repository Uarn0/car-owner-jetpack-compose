package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM events WHERE vehicleId = :vehicleId ORDER BY globalEventId DESC ")
    fun observeEvents(vehicleId: Long): Flow<List<EventEntity>>

    @Insert
    suspend fun insert(event: EventEntity): Long

    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteById(id: Long)
}