package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDAO {
    @Query("SELECT * FROM vehicles ORDER BY id DESC")
    fun observeVehicles(): Flow<List<VehiclesEntity>>
    @Insert
    suspend fun insert(vehicles: VehiclesEntity): Long
    @Delete
    suspend fun delete(vehicles: VehiclesEntity)
    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteById(id: Long)
}