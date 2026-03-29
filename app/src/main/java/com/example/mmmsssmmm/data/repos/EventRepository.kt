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

class EventRepository(private val db: AppDatabase){

    fun observeEvents(vehicleId: Long): Flow<List<VehicleHistoryItem>> {
        return db.eventDao()
            .observeFullEvents(vehicleId)
            .map { list ->
                list.map { fullEvent -> fullEvent.toDomain() }
            }
    }

    suspend fun insertBasicEvent(
        vehicleId: Long,
        name: String,
        date: String,
        odometer: Int,
        totalCost: Double
    ) {
        db.eventDao().insert(
            EventEntity(
                vehicleId = vehicleId,
                name = name,
                date = date,
                odometer = odometer,
                totalCost = totalCost
            )
        )
    }

    suspend fun insertTripEvent(
        vehicleId: Long, name: String, time: String, odometer: Int, totalCost: Double,
        startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean
    ) {
        db.withTransaction {
            val newEventId = db.eventDao().insert(
                EventEntity(
                    vehicleId = vehicleId,
                    name = name,
                    date = time,
                    odometer = odometer,
                    totalCost = totalCost
                )
            )

            db.tripDao().insert(
                TripEntity(
                    eventId = newEventId,
                    startPoint = startPoint,
                    endPoint = endPoint,
                    distanceKM = distanceKM,
                    isBusiness = isBusiness
                )
            )
        }
    }

    suspend fun insertServiceEvent(
        vehicleId: Long,
        name: String,
        date: String,
        odometer: Int,
        totalCost: Double,
        workTitle: String,
        serviceStation: String
    ) {
        db.withTransaction {
            val newEventId = db.eventDao().insert(
                EventEntity(
                    vehicleId = vehicleId,
                    name = name,
                    date = date,
                    odometer = odometer,
                    totalCost = totalCost
                )
            )

            db.serviceDao().insert(
                ServiceEntity(
                    eventId = newEventId,
                    workTitle = workTitle,
                    serviceStation = serviceStation
                )
            )
        }
    }

    suspend fun insertFuelingEvent(
        vehicleId: Long,
        name: String,
        date: String,
        odometer: Int,
        totalCost: Double,

        fuelTypeId: Int,
        volumeLiters: Double,
        pricePerLiter: Double,
        isFullTank: Boolean
    ) {
        db.withTransaction {

            val newEventId = db.eventDao().insert(
                EventEntity(
                    vehicleId = vehicleId,
                    name = name,
                    date = date,
                    odometer = odometer,
                    totalCost = totalCost
                )
            )

            db.fuelDao().insert(
                FuelingEntity(
                    eventId = newEventId,
                    fuelTypeId = fuelTypeId,
                    volumeLiters = volumeLiters,
                    pricePerLiter = pricePerLiter,
                    isFullTank = isFullTank
                )
            )
        }
    }

    suspend fun deleteInId(id: Long) = db.eventDao().deleteById(id)

    suspend fun serviceDeleteInId(id: Long) = db.serviceDao().delete(id)
    suspend fun tripDeleteInId(id: Long) = db.tripDao().delete(id)
    suspend fun fuelDeleteInId(id: Long) = db.fuelDao().delete(id)
}