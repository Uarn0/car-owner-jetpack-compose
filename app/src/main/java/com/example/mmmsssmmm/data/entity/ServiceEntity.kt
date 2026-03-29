package com.example.mmmsssmmm.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "service",
    foreignKeys = [
        ForeignKey(
            entity = EventEntity::class,
            parentColumns = ["globalEventId"],
            childColumns = ["eventId"],
            onDelete = ForeignKey.Companion.CASCADE
        )
    ], indices = [Index("eventId")])
data class ServiceEntity(
    @PrimaryKey val eventId: Long,
    val workTitle: String,
    val serviceStation: String,
)