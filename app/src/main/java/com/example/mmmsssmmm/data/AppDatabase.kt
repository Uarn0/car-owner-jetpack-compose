package com.example.mmmsssmmm.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.mmmsssmmm.data.dao.EventDAO
import com.example.mmmsssmmm.data.dao.FuelDAO
import com.example.mmmsssmmm.data.dao.ServiceDAO
import com.example.mmmsssmmm.data.dao.TripDAO
import com.example.mmmsssmmm.data.dao.VehiclesDAO
import com.example.mmmsssmmm.data.dictionary.BrandDictEntity
import com.example.mmmsssmmm.data.dictionary.BrandTypeDictEntity
import com.example.mmmsssmmm.data.dictionary.DatabaseCallback
import com.example.mmmsssmmm.data.dictionary.DictionaryDao
import com.example.mmmsssmmm.data.dictionary.FuelDictEntity
import com.example.mmmsssmmm.data.dictionary.ModelDictEntity
import com.example.mmmsssmmm.data.entity.EventEntity
import com.example.mmmsssmmm.data.entity.FuelingEntity
import com.example.mmmsssmmm.data.entity.ServiceEntity
import com.example.mmmsssmmm.data.entity.TripEntity
import com.example.mmmsssmmm.data.entity.VehiclesEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

@Database(
    entities = [
        VehiclesEntity::class,
        EventEntity::class,
        ServiceEntity::class,
        TripEntity::class,
        FuelingEntity::class,
        BrandDictEntity::class,
        BrandTypeDictEntity::class,
        FuelDictEntity::class,
        ModelDictEntity::class],
    version = 6,
    exportSchema = true
)

abstract class AppDatabase: RoomDatabase() {
    abstract fun vehicleDao(): VehiclesDAO
    abstract fun eventDao(): EventDAO
    abstract fun tripDao(): TripDAO
    abstract fun fuelDao(): FuelDAO
    abstract fun serviceDao(): ServiceDAO
    abstract fun dictionaryDao(): DictionaryDao



    companion object{

        @Volatile
        private var INSTANCE: AppDatabase? = null

        private val databaseScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

        fun getInstance(context: Context): AppDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "garage_v10.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(DatabaseCallback(context, databaseScope))
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}

//   <!______MIGRATION______!)

//@Database(
//    entities = [VehiclesEntity::class, EventEntity::class, ServiceEntity::class, TripEntity::class, FuelingEntity::class],
//    version = 3,
//    autoMigrations = [
//        AutoMigration(
//            from = 2,
//            to = 3,
//            spec = AppDatabase2.MyAutoMigration::class
//        )
//    ],
//    exportSchema = true,
//)
//abstract class AppDatabase2: RoomDatabase() {
//    class MyAutoMigration : AutoMigrationSpec
//
//    abstract fun vehicleDao(): VehiclesDAO
//    abstract fun eventDao(): EventDAO
//
//    @Volatile
//    private var INSTANCE2: AppDatabase2? = null
//
//    fun getInstance(context: Context): AppDatabase2{
//        return INSTANCE2 ?: synchronized(this){
//            val instance = Room.databaseBuilder(
//                context.applicationContext,
//                AppDatabase2::class.java,
//                "garage.db"
//            ).fallbackToDestructiveMigration().build()
//            INSTANCE2 = instance
//            instance
//        }
//    }
//}