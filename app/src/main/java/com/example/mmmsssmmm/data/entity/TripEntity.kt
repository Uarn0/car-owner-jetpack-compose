package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "trip",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["globalEventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ])
data class TripEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: Long,
    val startPoint: String,
    val endPoint: String,
    val distanceKM: Int,
    val isBusiness: Boolean,
)