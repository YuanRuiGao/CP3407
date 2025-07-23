// AppointmentScreen.kt
package com.example.myapplication_ass2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Message
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AppointmentScreen(
    navController: NavController,
    appointmentViewModel: AppointmentViewModel = viewModel()
) {
    val appointments by appointmentViewModel.appointments.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var selectedLocation by remember { mutableStateOf<String?>(null) }
    var selectedLevel by remember { mutableStateOf<String?>(null) }
    var date by remember { mutableStateOf("Select Date") }
    var time by remember { mutableStateOf("Select Time") }

    val locations = listOf("Living Room", "Bedroom", "Kitchen", "Bathroom", "Dining Room", "Balcony", "Study", "Whole House")
    val levels = listOf("Junior Cleaner", "Intermediate Cleaner", "Senior Cleaner", "Expert Cleaner", "Team (2 ppl)", "Deep Clean Specialist", "Move‑Out Specialist", "Window Cleaning")

    Box(Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().alpha(0.2f)
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(scrollState).padding(16.dp)
        ) {
            Text("Cleaner Appointment", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(Modifier.height(16.dp))

            Text("Choose Location:", fontSize = 16.sp)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                locations.forEach { loc ->
                    FilterChip(selected = (selectedLocation == loc), onClick = { selectedLocation = loc }, label = { Text(loc) })
                }
            }

            Spacer(Modifier.height(16.dp))

            Text("Choose Cleaner Level / Service:", fontSize = 16.sp)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                levels.forEach { lvl ->
                    FilterChip(selected = (selectedLevel == lvl), onClick = { selectedLevel = lvl }, label = { Text(lvl) })
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                val cal = Calendar.getInstance()
                DatePickerDialog(context, { _, year, month, day ->
                    val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    cal.set(year, month, day)
                    date = fmt.format(cal.time)
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text(date) }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                val cal = Calendar.getInstance()
                TimePickerDialog(context, { _, h, _ -> time = "%02d:00".format(h) }, cal.get(Calendar.HOUR_OF_DAY), 0, true).show()
            }, modifier = Modifier.align(Alignment.CenterHorizontally)) { Text(time) }

            Spacer(Modifier.height(16.dp))

            Button(onClick = {
                appointmentViewModel.addAppointment(selectedLocation!!, selectedLevel!!, date, time)

                val appointmentDetails = """
                    Location: $selectedLocation
                    Cleaner Level: $selectedLevel
                    Date: $date
                    Time: $time
                    ---------------------------
                """.trimIndent()
                val file = File(context.filesDir, "appointments.txt")
                FileOutputStream(file, true).use { output ->
                    output.write(appointmentDetails.toByteArray())
                    output.write("\n".toByteArray())
                }

                selectedLocation = null
                selectedLevel = null
                date = "Select Date"
                time = "Select Time"
            }, enabled = (selectedLocation != null && selectedLevel != null && date != "Select Date" && time != "Select Time"), modifier = Modifier.align(Alignment.CenterHorizontally), shape = RoundedCornerShape(12.dp)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add Appointment")
            }

            Spacer(Modifier.height(24.dp))

            appointments.forEach { appt ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(4.dp)) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Location: ${appt.location}", style = MaterialTheme.typography.titleMedium)
                            Text("Service: ${appt.cleanerLevel}", fontSize = 14.sp)
                            Text("Date: ${appt.date}", fontSize = 14.sp)
                            Text("Time: ${appt.time}", fontSize = 14.sp)
                        }
                        IconButton(onClick = { navController.navigate("communication") }) {
                            Icon(Icons.Default.Message, contentDescription = "Contact Cleaner", tint = Color(0xFF4CAF50))
                        }
                        IconButton(onClick = { appointmentViewModel.deleteAppointment(appt) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(onClick = { navController.navigate("home") }, modifier = Modifier.fillMaxWidth().padding(top = 8.dp), shape = RoundedCornerShape(12.dp)) {
                Text("Back to Home", fontSize = 16.sp)
                Button(
                    onClick = { navController.navigate("appointment_log") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("View Appointment Log", fontSize = 16.sp)
                }
            }
        }
    }
}
