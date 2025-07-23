package com.example.myapplication_ass2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun PaymentScreen(
    navController: NavController,
    paymentViewModel: PaymentViewModel = viewModel()
) {
    val payments by paymentViewModel.payments.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }

    val pending = payments.filter { !it.taken }
    val completed = payments.filter { it.taken }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.b),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Payment Schedule",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (pending.isNotEmpty()) {
                item { Text("Pending Payments", style = MaterialTheme.typography.headlineSmall) }
                items(pending) { p ->
                    AnimatedVisibility(
                        visible = true,
                        exit = fadeOut(animationSpec = tween(500)) +
                                shrinkVertically(animationSpec = tween(500))
                    ) {
                        PaymentCard(p) { paymentViewModel.markAsDone(p) }
                    }
                }
            }
            if (completed.isNotEmpty()) {
                item { Text("Completed Payments", style = MaterialTheme.typography.headlineSmall) }
                items(completed) { p -> CompletedPaymentCard(p) }
            }
            item {
                val total = payments.size
                val done = completed.size
                val progress = if (total > 0) done.toFloat() / total else 0f
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Progress: ${(progress * 100).toInt()}%", fontSize = 20.sp)
                    Spacer(Modifier.height(4.dp))
                    LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
                }
            }
            item {
                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Back to Home", fontSize = 18.sp, color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add Payment")
        }

        if (showDialog) {
            AddPaymentDialog(
                onAdd = { name, amount, time ->
                    paymentViewModel.addPayment(name, amount, time)
                    showDialog = false
                },
                onDismiss = { showDialog = false }
            )
        }
    }
}

@Composable
fun PaymentCard(payment: PaymentEntity, onDone: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(payment.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Amount: ${"%.2f".format(payment.amount)} SGD", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("Time: ${payment.time}", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Button(onClick = onDone) {
                Text("Mark as Paid")
            }
        }
    }
}

@Composable
fun CompletedPaymentCard(payment: PaymentEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(payment.name, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text("Amount: ${"%.2f".format(payment.amount)} SGD", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(4.dp))
            Text("Time: ${payment.time}", style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(4.dp))
            Text("Status: Paid", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun AddPaymentDialog(onAdd: (String, Double, String) -> Unit, onDismiss: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    onAdd(name, amt, time)
                },
                enabled = name.isNotBlank() && amount.isNotBlank() && time.isNotBlank()
            ) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add Payment") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount (SGD)") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(12.dp)
    )
}
