package com.example.mmmsssmmm.data.dao
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.mmmsssmmm.data.entity.ServiceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceDAO {
    @Query("SELECT eventType FROM events ORDER BY globalEventId DESC")
    fun observeServices(): Flow<List<ServiceEntity>>

    @Insert
    suspend fun insert(service: ServiceEntity)

    @Query("DELETE FROM events WHERE globalEventId = :id")
    suspend fun delete(id: Long)
}
