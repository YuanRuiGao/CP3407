package com.example.myapplication_ass2

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AppointmentHistoryScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()
    val userId = auth.currentUser?.uid
    var cleaningRecords by remember { mutableStateOf(listOf<CleaningRecord>()) }
    var selectedMonth by remember { mutableStateOf(currentMonth()) }

    // Fetch on month change
    LaunchedEffect(selectedMonth) {
        if (userId != null) {
            cleaningRecords = fetchCleaningHistory(firestore, userId, selectedMonth)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween // ← 确保底部按钮贴底
    ) {

        Column {
            Text("Appointment History", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(8.dp))
            MonthDropdown(selectedMonth) { selectedMonth = it }

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f, fill = false) // ← 允许列表高度自适应
            ) {
                items(cleaningRecords) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Date: ${record.date}")
                                Text("Service: ${record.service}")
                                Text("Cleaner Level: ${record.cleanerLevel}")
                                Text("Price: ${record.price} SGD")
                            }
                            IconButton(onClick = {
                                userId?.let {
                                    deleteCleaningRecord(firestore, it, record)
                                    cleaningRecords = cleaningRecords.filterNot { it == record }
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }
            }
        }

        // ✅ Bottom Back to Home Button
        Button(
            onClick = { navController.navigate("home") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(top = 12.dp)
        ) {
            Text("Back to Home", color = Color.White, fontSize = 16.sp)
        }
    }
}

@Composable
fun MonthDropdown(current: String, onMonthSelected: (String) -> Unit) {
    val months = getRecentMonths()
    var expanded by remember { mutableStateOf(false) }

    Box {
        OutlinedButton(onClick = { expanded = true }) {
            Text(current)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            months.forEach {
                DropdownMenuItem(
                    text = { Text(it) },
                    onClick = {
                        onMonthSelected(it)
                        expanded = false
                    }
                )
            }
        }
    }
}

fun currentMonth(): String {
    val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    return sdf.format(Date())
}

fun getRecentMonths(): List<String> {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    return (0..5).map {
        val result = sdf.format(calendar.time)
        calendar.add(Calendar.MONTH, -1)
        result
    }
}

suspend fun fetchCleaningHistory(
    firestore: FirebaseFirestore,
    userId: String,
    month: String
): List<CleaningRecord> {
    val result = firestore.collection("appointments")
        .whereEqualTo("userId", userId)
        .get()
        .await()

    return result.documents.mapNotNull {
        val date = it.getString("date") ?: return@mapNotNull null
        if (!date.startsWith(month)) return@mapNotNull null
        CleaningRecord(
            id = it.id,
            date = date,
            service = it.getString("location") ?: "",
            cleanerLevel = it.getString("cleanerLevel") ?: "",
            price = it.getDouble("price")?.toInt() ?: 0
        )
    }
}

fun deleteCleaningRecord(
    firestore: FirebaseFirestore,
    userId: String,
    record: CleaningRecord
) {
    firestore.collection("appointments").document(record.id).delete()
}

data class CleaningRecord(
    val id: String,
    val date: String,
    val service: String,
    val cleanerLevel: String,
    val price: Int
)
