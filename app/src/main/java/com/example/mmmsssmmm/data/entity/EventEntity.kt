package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.mmmsssmmm.data.entity.VehiclesEntity

@Entity(
    tableName = "events", foreignKeys = [ForeignKey(
        entity = VehiclesEntity::class,
        parentColumns = ["id"],
        childColumns = ["vehicleId"],
        onDelete = ForeignKey.Companion.CASCADE
    )], indices = [Index("vehicleId")]
)

data class  EventEntity(
    @PrimaryKey(autoGenerate = true) val globalEventId: Long = 0,
    val vehicleId: Long,
    val name: String,
    val date: String,
    val odometer: Int,
    val totalCost: Double
)