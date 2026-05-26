package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.mmmsssmmm.data.fulldetails.FullEventDetails
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.tuples.CTOTuple
import com.example.mmmsssmmm.data.tuples.CarDropdownTuple
import com.example.mmmsssmmm.data.tuples.ComplexServiceStatTuple
import com.example.mmmsssmmm.data.tuples.CostPerKmTuple
import com.example.mmmsssmmm.data.tuples.CtoCostByCarTuple
import com.example.mmmsssmmm.data.tuples.CtoStationCountTuple
import com.example.mmmsssmmm.data.tuples.FuelTypeCostTuple
import com.example.mmmsssmmm.data.tuples.MonthlyCostTuple
import com.example.mmmsssmmm.data.tuples.MostTraveledVehicleTuple
import com.example.mmmsssmmm.data.tuples.PopularRouteTuple
import com.example.mmmsssmmm.data.tuples.TotalCostForFuelTuple
import kotlinx.coroutines.flow.Flow

@Dao
interface EventDAO {
    @Transaction

    @Query("SELECT * FROM events WHERE globalEventId = :eventId")
    fun observeSingleEvent(eventId: Long): Flow<FullEventDetails?>

    @Query("SELECT * FROM events WHERE vehicleId = :vehicleId ORDER BY date DESC")
    fun observeBaseEvents(vehicleId: Long): Flow<List<EventEntity>>

    @Query("SELECT tankCapacity FROM vehicles WHERE globalVehicleId = :carId")
    suspend fun getCarCapacity(carId: Long?): Double?


    @Query("SELECT vehicleId FROM events WHERE globalEventId = :eventId")
    suspend fun getCarId(eventId: Long): Long?


    @Query(
        """
        SELECT 
            v.plateNumber AS plateNumber, 
            m.name AS carName, 
            e.date AS date, 
            f.fuelTypeId AS fuelType, 
            (f.volumeLiters * f.pricePerLiter) AS totalCost 
        FROM events e
        INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
        INNER JOIN models m ON v.modelId = m.id    
        INNER JOIN fueling f ON e.globalEventId = f.eventId
    """
    )
    fun getFuelCostsHistory(): Flow<List<TotalCostForFuelTuple>>

    @Query(
        """
        SELECT 
            v.plateNumber AS plateNumber,
            m.name AS carName, 
            e.date AS date, 
            ft.nameOfFuel AS fuelType, 
            (f.volumeLiters * f.pricePerLiter) AS totalCost 
        FROM events e
        INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN fueling f ON e.globalEventId = f.eventId
        inner join fuel_types ft on f.fuelTypeId = ft.id
        WHERE e.date BETWEEN :startDate AND :endDate
        ORDER BY e.date DESC
    """
    )
    fun getFuelHistoryBetweenDates(
        startDate: String,
        endDate: String,
    ): Flow<List<TotalCostForFuelTuple>>

    @Query(
        """
        SELECT 
            v.plateNumber AS plateNumber,
            m.name AS carName, 
            e.date AS date, 
            ft.nameOfFuel AS fuelType,  
            (f.volumeLiters * f.pricePerLiter) AS totalCost
        FROM events e
        inner join fuel_types ft on f.fuelTypeId = ft.id
        INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN fueling f ON e.globalEventId = f.eventId
        WHERE (f.volumeLiters * f.pricePerLiter) = (
            SELECT MAX(volumeLiters * pricePerLiter) FROM fueling
        )
    """
    )
    fun getMostExpensiveFueling(): Flow<List<TotalCostForFuelTuple>>

    @Query(
        """
        SELECT 
            ft.nameOfFuel AS fuelType, 
            SUM(f.volumeLiters * f.pricePerLiter) AS totalCost
        FROM events e
        INNER JOIN fueling f ON e.globalEventId = f.eventId
        inner join fuel_types ft on f.fuelTypeId = ft.id
        WHERE f.fuelTypeId IS NOT NULL AND f.fuelTypeId != '' 
        GROUP BY f.fuelTypeId
    """
    )
    fun getTotalCostByFuelType(): Flow<List<FuelTypeCostTuple>>

    @Query(
        """
        SELECT 
            v.plateNumber AS plateNumber,
            m.name carName,
            e.date AS date,
            s.serviceStation AS station, 
            s.workTitle AS whatWork,
            cast(s.serviceCost AS Integer) AS cost
            FROM events e
            INNER JOIN vehicles v  ON e.vehicleId = v.globalVehicleId
            INNER JOIN models m ON v.modelId = m.id    
            INNER JOIN service s ON e.globalEventId = s.eventId
            WHERE s.workTitle LIKE '%' || :workTitle || '%'
    """
    )
    fun getCTOHistory(workTitle: String): Flow<List<CTOTuple>>

    @Query(
        """
        SELECT 
            m.name AS carName, 
            v.plateNumber AS plateNumber, 
            SUM(s.serviceCost) AS totalCost 
        FROM events e
        INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
        INNER JOIN models m ON v.modelId = m.id
        INNER JOIN service s ON e.globalEventId = s.eventId
        GROUP BY v.globalVehicleId
        ORDER BY totalCost DESC
    """
    )
    fun getCtoCostsByCar(): Flow<List<CtoCostByCarTuple>>

//    @Query(
//        """
//        SELECT
//            v.plateNumber AS plateNumber,
//            m.name AS carName,
//            e.date AS date,
//            s.serviceStation AS station,
//            s.workTitle AS whatWork,
//            s.serviceCost AS cost
//        FROM events e
//        INNER JOIN vehicles v ON e.vehicleId = v.globalVehicleId
//        INNER JOIN models m ON v.modelId = m.id
//        INNER JOIN service s ON e.globalEventId = s.eventId
//        WHERE s.serviceCost = (SELECT MAX(serviceCost) FROM service)
//    """
//    )
//    fun getMostExpensiveService(): Flow<List<CTOTuple>>

    @Query(
        """
        SELECT 
            s.serviceStation AS stationName, 
            COUNT(e.globalEventId) AS visitCount
        FROM events e
        INNER JOIN service s ON e.globalEventId = s.eventId
        GROUP BY s.serviceStation
        ORDER BY visitCount DESC
    """
    )
    fun getPopularStations(): Flow<List<CtoStationCountTuple>>


    @Query(
        """
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
"""
    )
    fun getLongestTripsHistory(): Flow<List<MostTraveledVehicleTuple>>

    @Query(
        """
        SELECT 
            t.startPoint AS startPoint, 
            t.endPoint AS endPoint, 
            COUNT(t.eventId) AS tripCount,
            SUM(t.distanceKM) AS totalRouteDistance
        FROM trip t
        WHERE t.startPoint != '' AND t.endPoint != '' 
        GROUP BY t.startPoint, t.endPoint
        ORDER BY tripCount DESC, totalRouteDistance DESC
    """
    )
    fun getPopularRoutes(): Flow<List<PopularRouteTuple>>

    @Query(
        """
        SELECT 
            IFNULL((
                SELECT SUM(f.volumeLiters * f.pricePerLiter) 
                FROM fueling f 
                INNER JOIN events e ON f.eventId = e.globalEventId WHERE e.vehicleId = :id), 0.0) +
            IFNULL((SELECT SUM(s.serviceCost) FROM service s INNER JOIN events e ON s.eventId = e.globalEventId WHERE e.vehicleId = :id), 0.0)
    """
    )
    fun getTotalSpentByCar(id: Long): Flow<Double?>

    @Query(
        """
        SELECT 
            strftime('%Y-%m', e.date) AS monthYear, 
            SUM(s.serviceCost) AS totalSpent
        FROM events e
        INNER JOIN service s ON e.globalEventId = s.eventId
        GROUP BY monthYear
        ORDER BY monthYear DESC
    """
    )
    fun getMonthlyServiceCosts(): Flow<List<MonthlyCostTuple>>

    @Query(
        """
        SELECT 
            IFNULL(
                (
                    IFNULL((SELECT SUM(f.volumeLiters * f.pricePerLiter) FROM fueling f INNER JOIN events e ON f.eventId = e.globalEventId WHERE e.vehicleId = :id), 0.0) +
                    IFNULL((SELECT SUM(s.serviceCost) FROM service s INNER JOIN events e ON s.eventId = e.globalEventId WHERE e.vehicleId = :id), 0.0)
                ) 
                / 
                NULLIF((SELECT SUM(t.distanceKM) FROM trip t INNER JOIN events e ON t.eventId = e.globalEventId WHERE e.vehicleId = :id), 0.0), 
            0.0) AS costPerKm
    """
    )
    fun getCostPerKilometer(id: Long): Flow<CostPerKmTuple>

    @Query(
        """
        SELECT 
            v.globalVehicleId AS carId, 
            m.name AS carName, 
            v.plateNumber AS plateNumber
        FROM vehicles v
        INNER JOIN models m ON v.modelId = m.id
    """
    )
    fun getCarsForDashboard(): Flow<List<CarDropdownTuple>>

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

    @Query(
        """
    SELECT s.serviceStation, SUM(s.serviceCost) as totalCost
    FROM service s
    INNER JOIN events e ON s.eventId = e.globalEventId
    WHERE e.date >= :fromDate
    GROUP BY s.serviceStation
    HAVING SUM(s.serviceCost) > :fromCost
    ORDER BY totalCost DESC
"""
    )
    fun getComplexServiceStats(fromDate: String, fromCost: Double): Flow<List<ComplexServiceStatTuple>>
}