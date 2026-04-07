package com.example.mmmsssmmm.data.repos

import androidx.room.withTransaction
import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.FuelingEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity
import com.example.mmmsssmmm.data.entity.TripEntity
import com.example.mmmsssmmm.data.toDomain
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class EventRepository(private val db: AppDatabase) {

    fun observeBaseEvents(vehicleId: Long): Flow<List<EventEntity>> {
        return db.eventDao().observeBaseEvents(vehicleId)
    }

    suspend fun addToEventStats(eventId: Long, cost: Double, odometer: Int) {
        db.eventDao().incrementEventStats(eventId, cost, odometer)
    }

    //    fun observeEvents(vehicleId: Long): Flow<List<VehicleHistoryItem>> {
//        return db.eventDao()
//            .observeFullEvents(vehicleId)
//            .map { list -> list.map { fullEvent -> fullEvent.toDomain() } }
//    }
    fun observeSingleSubEvent(eventId: Long): Flow<VehicleHistoryItem?> {
        return db.eventDao()
            .observeSingleEvent(eventId)
            .map { fullEvent -> fullEvent?.toDomain() }
    }
    suspend fun insertBasicEvent(
        vehicleId: Long, name: String, date: String, odometer: Int, totalCost: Double
    ): Long {
        return db.eventDao().insert(
            EventEntity(
                vehicleId = vehicleId, name = name, date = date,
                odometer = odometer, totalCost = totalCost
            )
        )
    }

    suspend fun insertTripDetails(
        eventId: Long, startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean
    ) {
        db.tripDao().insert(
            TripEntity(
                eventId = eventId, startPoint = startPoint, endPoint = endPoint,
                distanceKM = distanceKM, isBusiness = isBusiness
            )
        )
    }

    suspend fun insertServiceDetails(
        eventId: Long, workTitle: String, serviceStation: String
    ) {
        db.serviceDao().insert(
            ServiceEntity(
                eventId = eventId, workTitle = workTitle, serviceStation = serviceStation
            )
        )
    }

    suspend fun insertFuelingDetails(
        eventId: Long, fuelTypeId: Int, volumeLiters: Double, pricePerLiter: Double, isFullTank: Boolean
    ) {
        db.fuelDao().insert(
            FuelingEntity(
                eventId = eventId, fuelTypeId = fuelTypeId, volumeLiters = volumeLiters,
                pricePerLiter = pricePerLiter, isFullTank = isFullTank
            )
        )
    }

    suspend fun deleteInId(id: Long) = db.eventDao().deleteById(id)

    suspend fun serviceDeleteInId(id: Long) = db.serviceDao().delete(id)
    suspend fun tripDeleteInId(id: Long) = db.tripDao().delete(id)
    suspend fun fuelDeleteInId(id: Long) = db.fuelDao().delete(id)
}