package com.example.mmmsssmmm.data.dictionary

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.CarDictionaryJson
import com.example.mmmsssmmm.data.dao.EventDAO
import com.example.mmmsssmmm.data.dao.FuelDAO
import com.example.mmmsssmmm.data.dao.ServiceDAO
import com.example.mmmsssmmm.data.dao.TripDAO
import com.example.mmmsssmmm.data.dao.VehiclesDAO
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.FuelingEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity
import com.example.mmmsssmmm.data.entity.TripEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DatabaseCallback(
    private val context: Context,
    private val scope: CoroutineScope,
    private val databaseProvider: () -> AppDatabase
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        scope.launch(Dispatchers.IO) {
            try {
                Log.d("DatabaseCallback", "Початок парсингу JSON...")

                val jsonString = context.assets.open("cars_dict.json")
                    .bufferedReader()
                    .use { it.readText() }

                val jsonParser = Json { ignoreUnknownKeys = true }
                val dictionary = jsonParser.decodeFromString<CarDictionaryJson>(jsonString)

                val database = databaseProvider()
                val dictDao = database.dictionaryDao()

                database.withTransaction {
                    dictDao.insertBrands(dictionary.brands)
                    dictDao.insertFuelTypes(dictionary.fuelTypeDict)
                    dictDao.insertBodyTypes(dictionary.bodyTypes)
                    dictDao.insertModels(dictionary.models)
                }
                Log.d("DatabaseCallback", "Довідники успішно завантажено!")

                generateMockData(
                    vehiclesDao = database.vehicleDao(),
                    eventDao = database.eventDao(),
                    fuelDao = database.fuelDao(),
                    serviceDao = database.serviceDao(),
                    tripDao = database.tripDao()
                )
                Log.d("DatabaseCallback", "15 тестових машин та подій успішно додано!")

            } catch (e: Exception) {
                Log.e("DatabaseCallback", "Помилка заповнення БД: ${e.message}", e)
            }
        }
    }

    private suspend fun generateMockData(
        vehiclesDao: VehiclesDAO,
        eventDao: EventDAO,
        fuelDao: FuelDAO,
        serviceDao: ServiceDAO,
        tripDao: TripDAO
    ) {
        val diverseModelIds = listOf<Long>(1, 55, 120, 185, 250, 310, 390, 440, 530, 610, 700, 780, 850, 912, 1050)

        for (i in 1..15) {
            val vehicle = VehiclesEntity(
                modelId = diverseModelIds[i - 1],
                manufactureYear = 2005 + i,
                tankCapacity = 50.0 + i,
                plateNumber = "CA${1000 + i}AA"
            )
            val vehicleId = vehiclesDao.insert(vehicle)

            val fuelEvent = EventEntity(
                vehicleId = vehicleId,
                name = "Заправка",
                date = "2026-05-${i.toString().padStart(2, '0')}",
                odometer = 150000 + (i * 100),
                totalCost = 1500.0 + (i * 10)
            )
            val fuelEventId = eventDao.insert(fuelEvent)

            val isFull = i % 2 == 0

            val fuelVolume = if (isFull) (vehicle.tankCapacity - 5.0) else (10.0 + i)

            val fueling = FuelingEntity(
                eventId = fuelEventId,
                volumeLiters = fuelVolume,
                fuelTypeId = (i % 5) + 1,
                pricePerLiter = 54.50,
                isFullTank = isFull
            )
            fuelDao.insert(fueling)

            val serviceEvent = EventEntity(
                vehicleId = vehicleId,
                name = "Планове ТО",
                date = "2026-06-${i.toString().padStart(2, '0')}",
                odometer = 150500 + (i * 100),
                totalCost = 2500.0 + (i * 100)
            )
            val serviceEventId = eventDao.insert(serviceEvent)

            val service = ServiceEntity(
                eventId = serviceEventId,
                serviceStation = "СТО $i",
                workTitle = "Заміна масла та фільтрів",
                serviceCost = 2500.0 + (i * 100)
            )
            serviceDao.insert(service)

            val tripEvent = EventEntity(
                vehicleId = vehicleId,
                name = "Поїздка по справах",
                date = "2026-07-${i.toString().padStart(2, '0')}",
                odometer = 151000 + (i * 100),
                totalCost = 0.0
            )
            val tripEventId = eventDao.insert(tripEvent)

            val trip = TripEntity(
                eventId = tripEventId,
                startPoint = "Черкаси",
                endPoint = if (i % 2 == 0) "Київ" else "Сміла",
                distanceKM = 190 + i
            )
            tripDao.insert(trip)
        }
    }
}