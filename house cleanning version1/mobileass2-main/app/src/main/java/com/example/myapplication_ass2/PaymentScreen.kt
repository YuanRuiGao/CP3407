package com.example.myapplication_ass2

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PaymentScreen(
    navController: NavController,
    appointmentId: String
) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    var appointment by remember { mutableStateOf<Map<String, Any>?>(null) }
    var isPaid by remember { mutableStateOf(false) }

    // 获取预约信息
    LaunchedEffect(appointmentId) {
        Log.d("PaymentScreen", "Fetching appointment with ID: $appointmentId")
        db.collection("appointments")
            .document(appointmentId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    appointment = document.data
                    isPaid = document.getBoolean("paid") == true
                }
            }
            .addOnFailureListener {
                Log.e("PaymentScreen", "Failed to fetch appointment", it)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        appointment?.let {
            Text("Location: ${it["location"]}", style = MaterialTheme.typography.titleMedium)
            Text("Service: ${it["cleanerLevel"]}", style = MaterialTheme.typography.bodyMedium)
            Text("Date: ${it["date"]}")
            Text("Time: ${it["time"]}")
            Text("Price: \$${it["price"]}")

            Spacer(modifier = Modifier.height(20.dp))

            if (isPaid) {
                Text("Payment Completed ✅", color = MaterialTheme.colorScheme.primary)
            } else {
                Button(onClick = {
                    db.collection("appointments")
                        .document(appointmentId)
                        .update("paid", true)
                        .addOnSuccessListener {
                            isPaid = true
                        }
                        .addOnFailureListener {
                            Log.e("PaymentScreen", "Payment update failed", it)
                        }
                }) {
                    Text("Confirm Payment")
                }
            }
        } ?: Text("Loading appointment...")

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Back to Home")
        }
    }
}
