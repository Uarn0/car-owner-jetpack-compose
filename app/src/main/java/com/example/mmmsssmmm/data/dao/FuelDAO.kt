package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.FuelingEntity

@Dao
interface FuelDAO {
    @Insert
    suspend fun insert(fuel: FuelingEntity)

    @Query("DELETE FROM events WHERE globalEventId = :id")
    suspend fun delete(id: Long)
}