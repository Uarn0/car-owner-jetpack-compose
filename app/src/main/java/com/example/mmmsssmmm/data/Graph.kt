package com.example.mmmsssmmm.data

import android.content.Context
import com.example.mmmsssmmm.data.repos.EventRepository
import com.example.mmmsssmmm.data.repos.VehicleRepository

object Graph{
    @Volatile private var initialized = false
    lateinit var database: AppDatabase
        private set
    lateinit var vehicleRepo: VehicleRepository
        private set
    lateinit var eventRepo: EventRepository
        private set
    fun init(context: Context){
        if(!initialized){
            database = AppDatabase.getInstance(context)
            vehicleRepo = VehicleRepository(database)
            eventRepo = EventRepository(database)
            initialized = true
        }
    }
}