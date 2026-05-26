package com.example.mmmsssmmm.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mmmsssmmm.data.dictionary.FuelDictEntity
import com.example.mmmsssmmm.data.entity.FuelingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fuel: FuelingEntity)

    @Query("select * from fuel_types order by nameOfFuel asc")
    fun getAllFuelTypes(): Flow<List<FuelDictEntity>>

    @Query("DELETE FROM fueling WHERE eventId = :id")
    suspend fun delete(id: Long)
}