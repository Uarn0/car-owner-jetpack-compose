package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
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
    ],
    indices = [Index("eventId")]
)
data class TripEntity(
    @PrimaryKey val eventId: Long,
    val startPoint: String,
    val endPoint: String,
    val distanceKM: Int,
    val isBusiness: Boolean,
)