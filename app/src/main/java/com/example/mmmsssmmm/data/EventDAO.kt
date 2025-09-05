package com.example.mmmsssmmm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Query("SELECT * FROM events WHERE vehicleId = :vehicleId ORDER BY id DESC ")
    fun observeEvents(vehicleId: Long): Flow<List<EventEntity>>

    @Insert
    suspend fun insert(event: EventEntity): Long

    @Delete
    suspend fun delete(event: EventEntity)
}
