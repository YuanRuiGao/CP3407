// AppointmentEntity.kt
package com.example.myapplication_ass2

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointment_table")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val location: String,
    val cleanerLevel: String,
    val date: String,
    val time: String
)
