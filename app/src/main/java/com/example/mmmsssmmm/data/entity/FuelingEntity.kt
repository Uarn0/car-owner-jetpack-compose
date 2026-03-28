package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "fueling",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["globalEventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.Companion.CASCADE,
        )
    ], indices = [Index("eventId")])

data class FuelingEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val eventId: Long,
    val volumeLiters: Double,
    val pricePerLiter: Double,
    val isFullTank: Boolean,
)