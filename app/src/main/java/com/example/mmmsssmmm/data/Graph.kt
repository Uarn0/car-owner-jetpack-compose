package com.example.mmmsssmmm.data

import android.content.Context

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