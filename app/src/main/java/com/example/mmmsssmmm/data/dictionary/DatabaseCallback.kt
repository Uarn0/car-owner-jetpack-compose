package com.example.mmmsssmmm.data.dictionary

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import com.example.mmmsssmmm.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.security.Provider

class DatabaseCallback(
    private val scope: CoroutineScope,
    private val provider: () -> AppDatabase
) : RoomDatabase.Callback(){
    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)
        scope.launch(Dispatchers.IO) {
            populateDatabase(provider().dictionaryDao())
        }
    }
    private suspend fun populateDatabase(dao: DictionaryDao){
        val fuelTypes = listOf(
            FuelDictEntity(nameOfFuel = "Бензин A-95"),
            FuelDictEntity(nameOfFuel = "Бензин A-92"),
            FuelDictEntity(nameOfFuel = "Бензин A-98"),
            FuelDictEntity(nameOfFuel = "Дизель"),
            FuelDictEntity(nameOfFuel = "Газ (LPG)"),
            FuelDictEntity(nameOfFuel = "Електро")
        )
        dao.insertFuelTypes(fuelTypes)


    }
}