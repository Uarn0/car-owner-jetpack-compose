package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.relationship.EventAndFueling
import com.example.mmmsssmmm.data.relationship.EventAndService
import com.example.mmmsssmmm.data.relationship.EventAndTrip
import com.example.mmmsssmmm.domain.vehicleModel.*
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem

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

fun EventAndTrip.toDomain(): VehicleHistoryItem.Trip {
    return VehicleHistoryItem.Trip(
        eventId = this.event.globalEventId,
        date = this.event.date,
        odometer = this.event.odometer,
        totalCost = this.event.totalCost,
        startPoint = this.trip!!.startPoint,
        endPoint = this.trip.endPoint,
        distanceKM = this.trip.distanceKM,
        isBusiness = this.trip.isBusiness
    )
}

fun EventAndFueling.toDomain(): VehicleHistoryItem.Fueling{
    return VehicleHistoryItem.Fueling(
        eventId = this.event.globalEventId,
        date = this.event.date,
        odometer = this.event.odometer,
        totalCost = this.event.totalCost,
        volumeLiters = this.fueling!!.volumeLiters,
        pricePerLiter = this.fueling.pricePerLiter,
        isFullTank = this.fueling.isFullTank
    )
}

fun EventAndService.toDomain(): VehicleHistoryItem.Service{
    return VehicleHistoryItem.Service(
        eventId = this.event.globalEventId,
        date = this.event.date,
        odometer = this.event.odometer,
        totalCost = this.event.totalCost,
        workTitle = this.service!!.workTitle,
        serviceStation = this.service.serviceStation
    )
}