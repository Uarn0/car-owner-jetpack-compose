package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.BrandTypeDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity

@Entity(
    tableName = "vehicles",
    foreignKeys = [
        ForeignKey(
            entity = ModelDictEntity::class,
            parentColumns = ["id"],
            childColumns = ["modelId"]
        )
    ],
    indices = [Index("modelId")]
)
data class VehiclesEntity(
    @PrimaryKey(autoGenerate = true) val globalVehicleId: Long = 0,
    val modelId: Long,
    val manufactureYear: Int,
    val tankCapacity: Double,
    val plateNumber: String,
    val userImageUri: String? = null
)