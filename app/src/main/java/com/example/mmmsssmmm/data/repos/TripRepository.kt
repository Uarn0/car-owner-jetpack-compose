package com.example.mmmsssmmm.data.repos

import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.entity.TripEntity

class TripRepository(private val db: AppDatabase) {

    suspend fun insert(startPoint: String, endPoint: String, distanceKM: Int, isBusiness: Boolean) {
        db.tripDao().insert(
            trip = TripEntity(
                startPoint = startPoint,
                endPoint = endPoint,
                distanceKM = distanceKM,
                isBusiness = isBusiness
            )
        )
    }

    suspend fun deleteInId(globalEventId: Long){
        db.tripDao().delete(globalEventId)
    }
}