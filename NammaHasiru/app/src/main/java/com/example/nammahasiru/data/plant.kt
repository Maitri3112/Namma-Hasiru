package com.example.nammahasiru.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val speciesName: String,
    val latitude: Double,
    val longitude: Double,
    val plantedDate: Long = System.currentTimeMillis(),
    val status: String = "Planted",
    val photoPath: String = "",
    val village: String = ""
)