package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.data.entity.VehiclesEntity
import com.example.mmmsssmmm.data.fulldetails.FullEventDetails
import com.example.mmmsssmmm.data.fulldetails.FullVehicleDetails
import com.example.mmmsssmmm.domain.vehicleModel.*
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem

fun FullVehicleDetails.toDomain(): Vehicless {
    val v = this.vehicle
    val brandName = this.brand.brandName
    val modelName = this.model.name
    val typeName = this.type.typeName
    val imageName = this.model.imageResName

    return when (this.model.bodyTypeId) {
        // Припускаємо, що в JSON: 1 - Седан, 2 - Кросовер, 3 - Хетчбек і т.д.
        1, 2, 3, 5 -> Vehicless.Car(
            id = v.id,
            brand = brandName,
            model = modelName,
            type = typeName,
            image = imageName,
            year = v.manufactureYear,
            tankCapacity = v.tankCapacity
        )
        4 -> Vehicless.Minibus(
            id = v.id,
            brand = brandName,
            model = modelName,
            type = typeName,
            image = imageName,
            year = v.manufactureYear,
        )
        else -> Vehicless.Motorcycle(
            id = v.id,
            brand = brandName,
            model = modelName,
            type = typeName,
            image = imageName,
            year = v.manufactureYear
        )
    }
}
//
//fun EventAndTrip.toDomain(): VehicleHistoryItem.Trip {
//    return VehicleHistoryItem.Trip(
//        eventId = this.event.globalEventId,
//        date = this.event.date,
//        odometer = this.event.odometer,
//        totalCost = this.event.totalCost,
//        startPoint = this.trip!!.startPoint,
//        endPoint = this.trip.endPoint,
//        distanceKM = this.trip.distanceKM,
//        isBusiness = this.trip.isBusiness
//    )
//}
//
//fun EventAndFueling.toDomain(): VehicleHistoryItem.Fueling{
//    return VehicleHistoryItem.Fueling(
//        eventId = this.event.globalEventId,
//        date = this.event.date,
//        odometer = this.event.odometer,
//        totalCost = this.event.totalCost,
//        volumeLiters = this.fueling!!.volumeLiters,
//        pricePerLiter = this.fueling.pricePerLiter,
//        isFullTank = this.fueling.isFullTank
//    )
//}
//
//fun EventAndService.toDomain(): VehicleHistoryItem.Service{
//    return VehicleHistoryItem.Service(
//        eventId = this.event.globalEventId,
//        date = this.event.date,
//        odometer = this.event.odometer,
//        totalCost = this.event.totalCost,
//        workTitle = this.service!!.workTitle,
//        serviceStation = this.service.serviceStation
//    )
//}

fun FullEventDetails.toDomain(): VehicleHistoryItem {
    return when {
        this.trip != null -> {
            VehicleHistoryItem.Trip(
                eventId = this.event.globalEventId,
                date = this.event.date,
                odometer = this.event.odometer,
                totalCost = this.event.totalCost,
                startPoint = this.trip.startPoint,
                endPoint = this.trip.endPoint,
                distanceKM = this.trip.distanceKM,
                isBusiness = this.trip.isBusiness
            )
        }
        this.fueling != null -> {
            VehicleHistoryItem.Fueling(
                eventId = this.event.globalEventId,
                date = this.event.date,
                odometer = this.event.odometer,
                totalCost = this.event.totalCost,
                volumeLiters = this.fueling.volumeLiters,
                pricePerLiter = this.fueling.pricePerLiter,
                isFullTank = this.fueling.isFullTank
            )
        }
        this.service != null -> {
            VehicleHistoryItem.Service(
                eventId = this.event.globalEventId,
                date = this.event.date,
                odometer = this.event.odometer,
                totalCost = this.event.totalCost,
                workTitle = this.service.workTitle,
                serviceStation = this.service.serviceStation
            )
        }
        else -> {
            throw IllegalStateException("Event details are missing for event ID: ${this.event.globalEventId}")
        }
    }
}