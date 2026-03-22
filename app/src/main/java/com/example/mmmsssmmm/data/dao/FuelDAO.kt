package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.FuelingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelDAO {
    @Query("SELECT eventType FROM events ORDER BY globalEventId DESC")
    fun observeFuels(): Flow<List<FuelingEntity>>

    @Insert
    suspend fun insert(fuel: FuelingEntity)

    @Query("DELETE FROM events WHERE globalEventId = :id")
    suspend fun delete(id: Long)
}