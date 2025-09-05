package com.example.mmmsssmmm.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDAO {
    @Query("SELECT * FROM vehicles ORDER BY id DESC")
    fun observeVehicles(): Flow<List<VehiclesEntity>>

    @Insert
    suspend fun insert(vehicles: VehiclesEntity): Long

    @Delete
    suspend fun delete(vehicles: VehiclesEntity)
}