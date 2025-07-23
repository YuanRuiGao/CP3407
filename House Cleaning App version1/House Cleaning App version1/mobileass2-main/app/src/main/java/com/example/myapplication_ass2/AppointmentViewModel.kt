package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileWriter
import java.io.IOException

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).appointmentDao()
    private val paymentDao = AppDatabase.getInstance(application).paymentDao()
    private val appContext = application.applicationContext

    private val _appointments = MutableStateFlow<List<AppointmentEntity>>(emptyList())
    val appointments: StateFlow<List<AppointmentEntity>> = _appointments

    init {
        viewModelScope.launch {
            dao.getAllAppointments().collect { list ->
                _appointments.value = list
            }
        }
    }

    fun addAppointment(location: String, cleanerLevel: String, date: String, time: String) {
        viewModelScope.launch {
            val appointment = AppointmentEntity(
                location = location,
                cleanerLevel = cleanerLevel,
                date = date,
                time = time
            )
            dao.insertAppointment(appointment)
            writeAppointmentToFile(appointment)

            val price = calculatePrice(location, cleanerLevel)
            paymentDao.insertPayment(
                PaymentEntity(
                    name = "$location ($cleanerLevel)",
                    amount = price,
                    time = "$date $time"
                )
            )
        }
    }

    fun deleteAppointment(appointment: AppointmentEntity) {
        viewModelScope.launch {
            dao.deleteAppointment(appointment)
        }
    }

    private fun writeAppointmentToFile(appointment: AppointmentEntity) {
        try {
            val file = File(appContext.filesDir, "output.txt")
            val writer = FileWriter(file, true)
            writer.appendLine("Location: ${appointment.location}, Service: ${appointment.cleanerLevel}, Date: ${appointment.date}, Time: ${appointment.time}")
            writer.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun readAppointmentsFromFile(): String {
        return try {
            val file = File(appContext.filesDir, "output.txt")
            if (file.exists()) {
                file.readText()
            } else {
                "No records found."
            }
        } catch (e: IOException) {
            e.printStackTrace()
            "Error reading file."
        }
    }

    private fun calculatePrice(location: String, cleanerLevel: String): Double {
        val basePrices = mapOf(
            "Living Room" to 50,
            "Bedroom" to 40,
            "Kitchen" to 60,
            "Bathroom" to 45,
            "Dining Room" to 35,
            "Balcony" to 30,
            "Study" to 40,
            "Whole House" to 120
        )
        val levelMultipliers = mapOf(
            "Junior Cleaner" to 1.0,
            "Intermediate Cleaner" to 1.2,
            "Senior Cleaner" to 1.4,
            "Expert Cleaner" to 1.6,
            "Team (2 ppl)" to 1.8,
            "Deep Clean Specialist" to 2.0,
            "Move‑Out Specialist" to 2.2,
            "Window Cleaning" to 1.5
        )
        val base = basePrices[location] ?: 0
        val multiplier = levelMultipliers[cleanerLevel] ?: 1.0
        return base * multiplier
    }
}
