package com.example.nammahasiru.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PlantDao {
    @Insert suspend fun insert(plant: Plant)
    @Update suspend fun update(plant: Plant)
    @Query("SELECT * FROM plants ORDER BY plantedDate DESC")
    fun getAllPlants(): LiveData<List<Plant>>
    @Query("SELECT COUNT(*) FROM plants")
    suspend fun getTotal(): Int
    @Query("SELECT COUNT(*) FROM plants WHERE status = 'Sprouted'")
    suspend fun getSprouted(): Int
}