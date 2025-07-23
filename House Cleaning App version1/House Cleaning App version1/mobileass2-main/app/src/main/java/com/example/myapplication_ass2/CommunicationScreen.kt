package com.example.myapplication_ass2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

/**
 * 请确保已经把图片复制到:
 * app/src/main/res/drawable/communication_bg.jpg
 */
@Composable
fun FamilyCommunicationScreen(
    navController: NavController
) {
    val settingsViewModel: AppSettingsViewModel = viewModel()
    val isDark = settingsViewModel.isDarkMode.value
    val fgColor = if (isDark) Color.White else Color.Black

    val msgVm: MessageViewModel = viewModel()
    val messages by msgVm.messages.collectAsStateWithLifecycle(emptyList())
    var input by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.fillMaxSize()) {
        // 底图
        Image(
            painter = painterResource(R.drawable.communication_bg),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.3f),            // 底图透明度
            contentScale = ContentScale.Crop
        )
        // 黑色遮罩，无论日夜都有效
        Box(
            Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
        )

        Column(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Cleaner Communication",
                style = MaterialTheme.typography.headlineMedium,
                color = fgColor,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(Modifier.height(12.dp))

            LazyColumn(
                Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(messages) { m ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = if (m.sender == "Me") Arrangement.End else Arrangement.Start
                    ) {
                        Column(
                            Modifier
                                .widthIn(max = 300.dp)
                                .background(
                                    if (m.sender == "Me") Color(0xFFBBDEFB)
                                    else MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp)
                        ) {
                            if (m.sender != "Me") {
                                Text(m.sender, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                            }
                            Text(
                                m.text,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (m.sender == "Me") Color.Black else fgColor
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(12.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = input,
                    onValueChange = { input = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                        .padding(12.dp)
                )
                Spacer(Modifier.width(8.dp))
                IconButton(onClick = {
                    input.text.takeIf { it.isNotBlank() }?.let {
                        msgVm.addMessage("Me", it)
                        input = TextFieldValue("")
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                }
            }

            Button(
                onClick = { navController.navigate("home") },
                Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Home", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}
