package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.FuelingEntity

@Dao
interface FuelDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fuel: FuelingEntity)

    @Query("DELETE FROM fueling WHERE eventId = :id")
    suspend fun delete(id: Long)
}