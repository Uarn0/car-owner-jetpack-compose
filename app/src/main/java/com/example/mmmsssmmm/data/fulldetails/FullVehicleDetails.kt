package com.example.mmmsssmmm.data.fulldetails

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.BrandTypeDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity

data class FullVehicleDetails(
    @Embedded val vehicle: VehiclesEntity,

    @Relation(parentColumn = "brandId", entityColumn = "id")
    val brand: BrandDictEntity,

    @Relation(parentColumn = "modelId", entityColumn = "id")
    val model: ModelDictEntity,

    @Relation(parentColumn = "typeId", entityColumn = "id")
    val type: BrandTypeDictEntity
)
