package com.example.mmmsssmmm.data.dictionary
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface DictionaryDao {


    @Query("SELECT * FROM fuel_types ORDER BY nameOfFuel ASC")
    fun observeFuelTypes(): Flow<List<FuelDictEntity>>

    @Query("SELECT * FROM brands ORDER BY brandName ASC")
    fun observeBrands(): Flow<List<BrandDictEntity>>

    @Query("SELECT * FROM body_types ORDER BY typeName ASC")
    fun observeBodyTypes(): Flow<List<BrandTypeDictEntity>>

    @Query("SELECT * FROM models WHERE brandId = :brandId ORDER BY name ASC")
    fun observeModels(brandId: Long): Flow<List<ModelDictEntity>>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBrands(brands: List<BrandDictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModels(models: List<ModelDictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFuelTypes(fuelTypes: List<FuelDictEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBodyTypes(bodyTypes: List<BrandTypeDictEntity>)
}