package com.example.mmmsssmmm.data.repos

import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.toDomain
import kotlinx.coroutines.flow.map

class VehicleRepository(private val db: AppDatabase){
    fun observerVehicle() = db.vehicleDao()
        .observeVehicles()
        .map { it.map { e -> e.toDomain() } }

    suspend fun insert(brandId: Int, model: String, manufactureYear: Int, tankCapacity: Double, plateNumber: String, typeOfVehicle: Int){
        db.vehicleDao().insert(
            VehiclesEntity(
                brandId = brandId,
                model = model,
                type = typeOfVehicle,
                manufactureYear = manufactureYear,
                tankCapacity = tankCapacity,
                plateNumber = plateNumber
            )
        )
    }
    suspend fun deleteById(id: Long) = db.vehicleDao().deleteById(id)
}