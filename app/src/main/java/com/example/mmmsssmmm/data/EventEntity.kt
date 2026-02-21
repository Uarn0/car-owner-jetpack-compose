package com.example.mmmsssmmm.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "events", foreignKeys = [ForeignKey(
        entity = VehiclesEntity::class,
        parentColumns = ["id"],
        childColumns = ["vehicleId"],
        onDelete = ForeignKey.CASCADE
    )], indices = [Index("vehicleId")]
)

data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val vehicleId: Long,
    val name: String,
    val type: Int,
    val time: String,
)
