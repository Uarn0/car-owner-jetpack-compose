package com.example.mmmsssmmm.data.repos

import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails
import kotlinx.coroutines.flow.Flow

class VehicleRepository(private val db: AppDatabase) {

    fun observeAllVehiclesFull(): Flow<List<FullVehicleDetails>> {
        return db.vehicleDao().observeAllVehiclesFull()
    }

    suspend fun insert(
        modelId: Long,
        manufactureYear: Int,
        tankCapacity: Double,
        plateNumber: String,
        imageUri: String? = null
    ) {
        db.vehicleDao().insert(
            VehiclesEntity(
                modelId = modelId,
                manufactureYear = manufactureYear,
                tankCapacity = tankCapacity,
                plateNumber = plateNumber,
                userImageUri = imageUri
            )
        )
    }

    suspend fun deleteById(id: Long) = db.vehicleDao().deleteById(id)

    fun observeBrands(): Flow<List<BrandDictEntity>> {
        return db.dictionaryDao().observeBrands()
    }

    fun observeModels(brandId: Long): Flow<List<ModelDictEntity>> {
        return db.dictionaryDao().observeModels(brandId)
    }
}