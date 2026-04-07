package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mmmsssmmm.data.fulldetails.FullEventDetails
import com.example.mmmsssmmm.data.entity.EventEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Transaction
    @Query("SELECT * FROM events WHERE globalEventId = :eventId")
    fun observeSingleEvent(eventId: Long): Flow<FullEventDetails?>
    @Query("SELECT * FROM events WHERE vehicleId = :vehicleId ORDER BY date DESC")
    fun observeBaseEvents(vehicleId: Long): Flow<List<EventEntity>>
    @Insert
    suspend fun insert(event: EventEntity): Long

    @Query("UPDATE events SET totalCost = :cost WHERE globalEventId = :id")
    suspend fun updateCost(id: Long, cost: Double)
    @Query("UPDATE events SET totalCost = totalCost + :addedCost, odometer = odometer + :addedOdo WHERE globalEventId = :id")
    suspend fun incrementEventStats(id: Long, addedCost: Double, addedOdo: Int)
    @Delete
    suspend fun delete(event: EventEntity)

    @Query("DELETE FROM events WHERE globalEventId = :id")
    suspend fun deleteById(id: Long)
    @Query("UPDATE events SET odometer = :odo WHERE globalEventId = :id")
    suspend fun updateOdometer(id: Long, odo: Int)
}