package com.example.mmmsssmmm.data.dictionary

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "fuel_types")
data class FuelDictEntity(
    @PrimaryKey(autoGenerate = false) val id: Long,
    @SerialName("name") val nameOfFuel: String
)