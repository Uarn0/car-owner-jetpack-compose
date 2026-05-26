package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails
import com.example.mmmsssmmm.data.tuples.CtoStationCountTotalTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiclesDAO {
    @Transaction
    @Query(
        """
        SELECT v.*, 
               b.brandName AS brandName, 
               m.name AS modelName, 
               t.typeName AS typeName
        FROM vehicles v
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN brands b ON m.brandId = b.id
        INNER JOIN body_types t ON m.bodyTypeId = t.id
    """
    )
    fun observeAllVehiclesFull(): Flow<List<FullVehicleDetails>>

    @Insert
    suspend fun insert(vehicles: VehiclesEntity): Long

    @Delete
    suspend fun delete(vehicles: VehiclesEntity)

    @Query("DELETE FROM vehicles WHERE globalVehicleId = :id")
    suspend fun deleteById(id: Long)

    @Query(
        """
    SELECT * FROM vehicles v 
        WHERE EXISTS (
            SELECT 1 FROM events e 
            INNER JOIN fueling f ON e.globalEventId = f.eventId 
            WHERE e.vehicleId = v.globalVehicleId
        )
    """
    )
    fun getCarsWithFuelings(): Flow<List<VehiclesEntity>>

    @Query("""
        SELECT serviceStation, SUM(serviceCost) as total
        FROM service 
        GROUP BY serviceStation 
        HAVING SUM(serviceCost) > AVG(serviceCost)
    """)
    fun getExpensiveStations(): Flow<List<CtoStationCountTotalTuple>>

    @Transaction
    @Query("""
        SELECT v.*, b.brandName AS brandName, m.name AS modelName, t.typeName AS typeName
        FROM vehicles v
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN brands b ON m.brandId = b.id
        INNER JOIN body_types t ON m.bodyTypeId = t.id
        WHERE b.brandName IN ('Nissan', 'Toyota', 'Honda', 'Mazda', 'Subaru')
    """)
    fun observeJdmCars(): Flow<List<FullVehicleDetails>>

    @Transaction
    @Query("""
        SELECT v.*, b.brandName AS brandName, m.name AS modelName, t.typeName AS typeName
        FROM vehicles v
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN brands b ON m.brandId = b.id
        INNER JOIN body_types t ON m.bodyTypeId = t.id
        WHERE v.manufactureYear >= 2015 OR v.tankCapacity >= 60
    """)
    fun observeModernOrLargeCapacityCars(): Flow<List<FullVehicleDetails>>

    @Transaction
    @Query("""
        SELECT v.*, b.brandName AS brandName, m.name AS modelName, t.typeName AS typeName
        FROM vehicles v
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN brands b ON m.brandId = b.id
        INNER JOIN body_types t ON m.bodyTypeId = t.id
        WHERE v.globalVehicleId IN (
            SELECT vehicleId FROM events e 
            INNER JOIN service s ON e.globalEventId = s.eventId
        )
    """)
    fun observeCarsWithServiceHistory(): Flow<List<FullVehicleDetails>>
}


