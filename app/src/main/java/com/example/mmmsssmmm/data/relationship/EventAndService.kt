package com.example.mmmsssmmm.data.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity

data class EventAndService(
    @Embedded val event: EventEntity,
    @Relation(
        parentColumn = "globalEventId",
        entityColumn = "eventId"
    )
    val service: ServiceEntity?
)
