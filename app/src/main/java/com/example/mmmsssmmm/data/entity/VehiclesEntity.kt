package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehicles")
data class VehiclesEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val brandId: Int,
    val model: String,
    val type: Int,
    val manufactureYear: Int,
    val tankCapacity: Double,
    val plateNumber: String
)