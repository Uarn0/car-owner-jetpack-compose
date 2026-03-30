package com.example.mmmsssmmm.data.repos

import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.toDomain
import com.example.mmmsssmmm.domain.item.Vehicless
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class VehicleRepository(private val db: AppDatabase){
    fun observerVehicle(): Flow<List<Vehicless>> {
        return db.vehicleDao()
            .observeAllVehiclesFull()
            .map { list ->
                list.map { fullVehicless -> fullVehicless.toDomain() }
            }
    }

    suspend fun insert(modelId: Long, manufactureYear: Int, tankCapacity: Double, plateNumber: String){
        db.vehicleDao().insert(
            VehiclesEntity(
                modelId = modelId,
                manufactureYear = manufactureYear,
                tankCapacity = tankCapacity,
                plateNumber = plateNumber,
            )
        )
    }
    suspend fun deleteById(id: Long) = db.vehicleDao().deleteById(id)
}