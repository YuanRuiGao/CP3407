package com.example.myapplication_ass2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ServiceDetailsScreen(navController: NavController) {
    val services = listOf(
        Triple("Living Room", "General vacuuming and dusting. Includes floor mopping and surface cleaning.", 50),
        Triple("Bedroom", "Change sheets, dusting, vacuuming and wiping down surfaces.", 40),
        Triple("Kitchen", "Sink, stove, countertops, microwave and fridge cleaning.", 60),
        Triple("Bathroom", "Toilet, shower, mirror and sink scrubbing and disinfection.", 45),
        Triple("Dining Room", "Table wiping, vacuuming, and floor mopping.", 35),
        Triple("Balcony", "Sweeping, window wiping and railing dusting.", 30),
        Triple("Study", "Desk and shelf cleaning, floor vacuuming.", 40),
        Triple("Whole House", "Complete package of all room types.", 120)
    )

    val cleanerLevels = listOf(
        Pair("Junior Cleaner", "Suitable for light and basic cleaning needs."),
        Pair("Intermediate Cleaner", "Experienced with standard home cleaning."),
        Pair("Senior Cleaner", "Capable of deep cleaning and heavy-duty tasks."),
        Pair("Expert Cleaner", "Professional-grade cleaning with quality assurance."),
        Pair("Team (2 ppl)", "Fast and efficient cleaning for bigger homes."),
        Pair("Deep Clean Specialist", "Expert in removing tough stains and sanitization."),
        Pair("Move‑Out Specialist", "Ideal for pre/post move cleanup."),
        Pair("Window Cleaning", "Dedicated window and glass cleaning professional.")
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.15f)
        )

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
                Spacer(Modifier.width(8.dp))
                Text("Service Details", style = MaterialTheme.typography.headlineMedium)
            }

            Spacer(Modifier.height(12.dp))
            Text("Cleaning Services:", fontSize = 20.sp, fontWeight = FontWeight.Bold)

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(services) { (title, desc, price) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(title, fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(4.dp))
                            Text(desc, fontSize = 14.sp)
                            Spacer(Modifier.height(4.dp))
                            Text("Base Price: $price SGD", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { navController.navigate("daily_routine") },
                                modifier = Modifier.align(Alignment.End),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Book Now!")
                            }
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(16.dp))
                    Text("Cleaner Levels:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }

                items(cleanerLevels) { (level, detail) ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEDF4F7))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(level, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                            Spacer(Modifier.height(4.dp))
                            Text(detail, fontSize = 14.sp)
                        }
                    }
                }

                item { Spacer(Modifier.height(20.dp)) }
            }
        }
    }
}
