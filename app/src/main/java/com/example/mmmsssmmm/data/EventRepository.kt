package com.example.mmmsssmmm.data

import kotlinx.coroutines.flow.map

class EventRepository(private val db: AppDatabase){
    fun observeEvents(vehicleId: Long) = db.eventDao()
        .observeEvents(vehicleId)
        .map { it.map { e -> e.toDomain() } }

    suspend fun insert(vehicleId: Long, name: String, type: Int, time: String){
        db.eventDao().insert(EventEntity(vehicleId = vehicleId, name = name, type = type, time = time))
    }

    suspend fun deleteById(id: Long) = db.eventDao().deleteById(id)
}