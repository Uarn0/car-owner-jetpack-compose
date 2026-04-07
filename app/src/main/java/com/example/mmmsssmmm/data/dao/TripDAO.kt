package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.TripEntity

@Dao
interface TripDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(trip: TripEntity): Long

    @Query("DELETE FROM trip WHERE eventId = :id")
    suspend fun delete(id: Long)
}