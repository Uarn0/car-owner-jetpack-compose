package com.example.mmmsssmmm.data

import kotlinx.coroutines.flow.map

class VehicleRepository(private val db: AppDatabase){
    fun observerVehicle() = db.vehicleDao()
        .observeVehicles()
        .map{ it.map { e -> e.toDomain() } }

    suspend fun insert(name: String, type: Int, image: Int){
        db.vehicleDao().insert(VehiclesEntity(name = name, type = type, image = image))
    }
    suspend fun deleteById(id: Long) = db.vehicleDao().deleteById(id)
}