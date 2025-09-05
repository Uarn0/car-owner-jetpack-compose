// data/Mappers.kt
package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.domain.vehicleModel.*
import com.example.mmmsssmmm.domain.eventModel.*

fun VehiclesEntity.toDomain(): IVehicle = when (type) {
    0 -> Car(id = id, name = name, image = image)
    1 -> Minibus(id = id, name = name, image = image)
    else -> Motorcycle(id = id, name = name, image = image)
}

fun EventEntity.toDomain(): IEvent = when (type) {
    0 -> RepairEvent(
        id = id,
        vehicleId = vehicleId,
        timeWhenAdded = time
    )
    1 -> TripEvent(
        id = id,
        vehicleId = vehicleId,
        timeWhenAdded = time
    )
    else -> ServiceEvent(
        id = id,
        vehicleId = vehicleId,
        timeWhenAdded = time
    )
}
