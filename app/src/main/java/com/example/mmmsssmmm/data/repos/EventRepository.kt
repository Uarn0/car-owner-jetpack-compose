package com.example.mmmsssmmm.data.repos

import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity
import com.example.mmmsssmmm.data.entity.TripEntity
import com.example.mmmsssmmm.data.toDomain
import kotlinx.coroutines.flow.map

class EventRepository(private val db: AppDatabase){
    fun observeEvents(vehicleId: Long) = db.eventDao()
        .observeEvents(vehicleId)
        .map { it.map { e -> e.toDomain() } }

    suspend fun insert(vehicleId: Long, name: String, type: Int, time: String){
        db.eventDao().insert(
            EventEntity(
                vehicleId = vehicleId,
                name = name,
                eventType = type,
                date = time
            )
        )
    }

    suspend fun insert(eventId: Long, startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean) {
        db.tripDao().insert(
            trip = TripEntity(
                eventId = eventId,
                startPoint = startPoint,
                endPoint = endPoint,
                distanceKM = distanceKM,
                isBusiness = isBusiness
            )
        )
    }

    suspend fun insert(eventId: Long, workTitle: String, serviceStation: String){
        db.serviceDao().insert(
            service = ServiceEntity(
                eventId = eventId,
                workTitle = workTitle,
                serviceStation = serviceStation
            )
        )
    }
    suspend fun deleteInId(id: Long) = db.eventDao().deleteById(id)

    suspend fun serviceDeleteInId(id: Long) = db.serviceDao().delete(id)
    suspend fun tripDeleteInId(id: Long) = db.tripDao().delete(id)
    suspend fun fuelDeleteInId(id: Long) = db.fuelDao().delete(id)
}