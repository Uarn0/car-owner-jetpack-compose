package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mmmsssmmm.data.fulldetails.FullEventDetails
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.tuples.CTOTuple
import com.example.mmmsssmmm.data.tuples.MostTraveledVehicleTuple
import com.example.mmmsssmmm.data.tuples.TotalCostForFuelTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Transaction

    @Query("SELECT * FROM events WHERE globalEventId = :eventId")
    fun observeSingleEvent(eventId: Long): Flow<FullEventDetails?>
    @Query("SELECT * FROM events WHERE vehicleId = :vehicleId ORDER BY date DESC")
    fun observeBaseEvents(vehicleId: Long): Flow<List<EventEntity>>
    @Query("""
    SELECT 
        v.plateNumber AS plateNumber, 
        m.name AS carName, 
        e.date AS date, 
        f.fuelTypeId AS fuelType, 
        CAST(e.totalCost AS INTEGER) AS totalCost 
    FROM events e
    INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
    INNER JOIN models m ON v.modelId = m.id    
    INNER JOIN fueling f ON e.globalEventId = f.eventId
""")
    fun getFuelCostsHistory(): Flow<List<TotalCostForFuelTuple>>

    @Query("""
        SELECT 
            v.plateNumber AS plateNumber,
            m.name carName,
            e.date AS date,
            s.serviceStation AS station, 
            s.workTitle AS whatWork,
            cast(e.totalCost AS Integer) AS cost
            FROM events e
            INNER JOIN vehicles v  ON e.vehicleId = v.globalVehicleId
            INNER JOIN models m ON v.modelId = m.id    
            INNER JOIN service s ON e.globalEventId = s.eventId
            WHERE s.workTitle LIKE '%' || :workTitle || '%'
    """)
    fun getCTOHistory(workTitle: String): Flow<List<CTOTuple>>

    @Query("""
    SELECT 
        v.plateNumber as plateNumber,
        m.name AS carName,       
        e.date AS dateWhenAdd, 
        t.distanceKM AS totalTraveledKM
    FROM events e
    INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
    INNER JOIN models m ON v.modelId = m.id    
    INNER JOIN trip t ON e.globalEventId = t.eventId
    ORDER BY t.distanceKM DESC   
""")
    fun getLongestTripsHistory(): Flow<List<MostTraveledVehicleTuple>>
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