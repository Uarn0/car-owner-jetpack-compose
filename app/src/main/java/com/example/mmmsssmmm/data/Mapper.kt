package com.example.mmmsssmmm.data

import com.example.mmmsssmmm.data.fulldetails.FullEventDetails
import com.example.mmmsssmmm.domain.item.VehicleHistoryItem
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
            VehicleHistoryItem.Base(
                eventId = this.event.globalEventId,
                date = this.event.date,
                odometer = this.event.odometer,
                totalCost = this.event.totalCost
            )
        }
    }
}