package com.example.mmmsssmmm.data.dictionary

import android.content.Context
import android.util.Log
import androidx.room.RoomDatabase
import androidx.room.withTransaction // Додано для suspend транзакцій
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.mmmsssmmm.data.AppDatabase
import com.example.mmmsssmmm.data.CarDictionaryJson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class DatabaseCallback(
    private val context: Context,
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {

    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        scope.launch(Dispatchers.IO) {
            delay(1000)
            try {
                val jsonString = context.assets.open("cars_dict.json")
                    .bufferedReader()
                    .use { it.readText() }

                val jsonParser = Json { ignoreUnknownKeys = true }
                val dictionary = jsonParser.decodeFromString<CarDictionaryJson>(jsonString)

                val database = AppDatabase.getInstance(context)
                val dao = database.dictionaryDao()

                database.withTransaction {
                    dao.insertBrands(dictionary.brands)
                    dao.insertFuelTypes(dictionary.fuelTypeDict)
                    dao.insertBodyTypes(dictionary.bodyTypes)
                    dao.insertModels(dictionary.models)
                }


            } catch (e: Exception) {
            }
        }
    }
}