package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDAO {
    @Transaction
    @Query("""
    SELECT v.*, 
           b.brandName AS brandName, 
           m.name AS modelName, 
           t.typeName AS typeName,
           m.imageResName AS imageResName
    FROM vehicles v
    INNER JOIN models m ON v.modelId = m.id
    INNER JOIN brands b ON m.brandId = b.id
    INNER JOIN body_types t ON m.bodyTypeId = t.id
""")
    fun observeAllVehiclesFull(): Flow<List<FullVehicleDetails>>
    @Insert
    suspend fun insert(vehicles: VehiclesEntity): Long
    @Delete
    suspend fun delete(vehicles: VehiclesEntity)
    @Query("DELETE FROM vehicles WHERE id = :id")
    suspend fun deleteById(id: Long)
}