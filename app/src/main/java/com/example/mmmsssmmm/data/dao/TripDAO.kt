package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.TripEntity

@Dao
interface TripDAO {
    @Insert
    suspend fun insert(trip: TripEntity): Long

    @Query("DELETE FROM events" +
            " WHERE globalEventId = :id")
    suspend fun delete(id: Long)
}