package com.example.myapplication_ass2

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// 数据类：用于表示每条预约记录
data class Appointment(
    val location: String = "",
    val cleanerLevel: String = "",
    val date: String = "",
    val time: String = "",
    val price: Double = 0.0
)

class AppointmentViewModel(application: Application) : AndroidViewModel(application) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _appointments = MutableStateFlow<List<Appointment>>(emptyList())
    val appointments: StateFlow<List<Appointment>> = _appointments

    init {
        listenToAppointments()
    }

    /** 添加预约 */
    fun addAppointment(location: String, cleanerLevel: String, date: String, time: String) {
        val currentUser = auth.currentUser ?: return

        val price = calculatePrice(location, cleanerLevel) // 计算价格
        val appointment = Appointment(location, cleanerLevel, date, time, price)

        val data = hashMapOf(
            "userId" to currentUser.uid,
            "location" to location,
            "cleanerLevel" to cleanerLevel,
            "date" to date,
            "time" to time,
            "price" to price,
            "createdAt" to Timestamp.now(),
        )

        firestore.collection("appointments")
            .add(data)
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    /** 删除预约 */
    fun deleteAppointment(appointment: Appointment) {
        val currentUser = auth.currentUser ?: return

        firestore.collection("appointments")
            .whereEqualTo("userId", currentUser.uid)
            .whereEqualTo("location", appointment.location)
            .whereEqualTo("cleanerLevel", appointment.cleanerLevel)
            .whereEqualTo("date", appointment.date)
            .whereEqualTo("time", appointment.time)
            .get()
            .addOnSuccessListener { result ->
                for (doc in result) {
                    firestore.collection("appointments").document(doc.id).delete()
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
            }
    }

    /** 从云端拉取当前用户的预约记录 */
    private fun listenToAppointments() {
        val currentUser = auth.currentUser ?: return

        firestore.collection("appointments")
            .whereEqualTo("userId", currentUser.uid)
            .orderBy("createdAt")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    e.printStackTrace()
                    return@addSnapshotListener
                }
                if (snapshots != null && !snapshots.isEmpty) {
                    val list = snapshots.map { doc ->
                        Appointment(
                            location = doc.getString("location") ?: "",
                            cleanerLevel = doc.getString("cleanerLevel") ?: "",
                            date = doc.getString("date") ?: "",
                            time = doc.getString("time") ?: "",
                            price = doc.getDouble("price") ?: 0.0
                        )
                    }
                    _appointments.value = list
                }
            }
    }

    /** 自动计算价格 */
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

