package com.example.mmmsssmmm.data.fulldetails

import androidx.room.Embedded
import androidx.room.Relation
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.FuelingEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity
import com.example.mmmsssmmm.data.entity.TripEntity

data class FullEventDetails(
    @Embedded val event: EventEntity,

    @Relation(parentColumn = "globalEventId", entityColumn = "eventId")
    val trip: TripEntity?,

    @Relation(parentColumn = "globalEventId", entityColumn = "eventId")
    val service: ServiceEntity?,

    @Relation(parentColumn = "globalEventId", entityColumn = "eventId")
    val fueling: FuelingEntity?
)