package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.domain.vehicleModel.*
import com.example.mmmsssmmm.domain.eventModel.*

fun VehiclesEntity.toDomain(): IVehicle = when (type) {
    0 -> Car(
        id = id,
        model = model,
        brandId = brandId,
        type = type,
        image = 0
    )
    1 -> Minibus(
        id = id,
        model = model,
        brandId = brandId,
        type = type,
        image = 1
    )
    else -> Motorcycle(
        id = id,
        model = model,
        brandId = brandId,
        type = type,
        image = 0
    )
}

fun EventEntity.toDomain(): IEvent {
    val event = when (eventType) {
        0 -> RepairEvent(
            id = globalEventId,
            vehicleId = vehicleId,
            name = name,
            type = eventType,
            timeWhenAdded = date
        )

        1 -> TripEvent(
            id = globalEventId,
            vehicleId = vehicleId,
            name = name,
            type = eventType,
            timeWhenAdded = date
        )

        else -> ServiceEvent(
            id = globalEventId,
            vehicleId = vehicleId,
            name = name,
            type = eventType,
            timeWhenAdded = date
        )
    }
    return event
}
