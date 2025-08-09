package com.example.myapplication_ass2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.myapplication_ass2.ui.theme.MyApplication_ass2Theme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val settingsViewModel: AppSettingsViewModel = viewModel()

            val user = FirebaseAuth.getInstance().currentUser
            val startDestination = if (user != null) "home" else "login"

            MyApplication_ass2Theme {
                Scaffold { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") { LoginScreen(navController) }
                        composable("register") { RegisterScreen(navController) }
                        composable("home") { HomeScreen(navController, settingsViewModel) }
                        composable("settings") { SettingsScreen(navController, settingsViewModel) }
                        composable("history") { AppointmentHistoryScreen(navController) }
                        composable("daily_routine") { AppointmentScreen(navController) }
                        composable("communication") { FamilyCommunicationScreen(navController) }
                        composable("service_details") { ServiceDetailsScreen(navController) } // ✅ 新增
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, settingsViewModel: AppSettingsViewModel) {
    val fontSize = settingsViewModel.fontSize.value
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.a),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            alpha = 0.5f
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(
                    Color.White.copy(alpha = 0.85f),
                    Color.White.copy(alpha = 0.4f)
                )))
        )
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "House-Cleaning Butler",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Color(0xFF2D2D2D)
                )
            }
            Spacer(Modifier.height(24.dp))
            Text(
                "Welcome to House-Cleaning Butler!",
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .background(Color.White.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            )
            Spacer(Modifier.height(24.dp))

            val options = listOf(
                Triple("Service Details", "service_details", Icons.Default.CleaningServices), // ✅ 调到最前
                Triple("Cleaner Appointment", "daily_routine", Icons.Default.List),
                Triple("Appointment History", "history", Icons.Default.History),
                Triple("Cleaner Communication", "communication", Icons.Default.Phone),
                Triple("App Settings", "settings", Icons.Default.Settings)
            )

            Column(modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally) {
                options.forEach { (text, route, icon) ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .height(80.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                        elevation = CardDefaults.cardElevation(6.dp),
                        onClick = { navController.navigate(route) }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.fillMaxSize().padding(20.dp)
                        ) {
                            Icon(icon, contentDescription = null, tint = Color(0xFF3A3A3A))
                            Spacer(Modifier.width(20.dp))
                            Text(text, fontSize = fontSize.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                }
                Spacer(Modifier.height(28.dp))

                // ✅ Logout button
                Button(
                    onClick = {
                        FirebaseAuth.getInstance().signOut()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(48.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("Log Out", color = Color.White, fontSize = 16.sp)
                }
            }
        }
    }
}
