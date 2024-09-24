package com.example.remindapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReminderApp()
        }
    }
}

@Composable
fun ReminderApp() {
    var reminderMessage by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var selectedTime by remember { mutableStateOf<Date?>(null) }
    var showSnackbar by remember { mutableStateOf(false) }
    var snackbarMessage by remember { mutableStateOf("") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val snackbarHostState = remember { SnackbarHostState() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = reminderMessage,
                    onValueChange = { reminderMessage = it },
                    label = { Text("Reminder Message") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                selectedDate = calendar.time
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }) {
                        Text("Select Date")
                    }

                    Button(onClick = {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)
                                selectedTime = calendar.time
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }) {
                        Text("Select Time")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (reminderMessage.isNotBlank() && selectedDate != null && selectedTime != null) {
                            snackbarMessage = "Reminder set successfully!"
                            showSnackbar = true
                        } else {
                            snackbarMessage = "Please fill all fields"
                            showSnackbar = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Set Reminder")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        reminderMessage = ""
                        selectedDate = null
                        selectedTime = null
                        snackbarMessage = "Reminder cleared"
                        showSnackbar = true
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Clear Reminder")
                }

                Spacer(modifier = Modifier.height(32.dp))

                if (selectedDate != null && selectedTime != null && reminderMessage.isNotBlank()) {
                    Text("Reminder: $reminderMessage", fontSize = 36.sp)
                    Text("Date: ${SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(selectedDate!!)}", fontSize = 36.sp)
                    Text("Time: ${SimpleDateFormat("HH:mm", Locale.getDefault()).format(selectedTime!!)}", fontSize = 36.sp)
                }
            }

            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }

    if (showSnackbar) {
        LaunchedEffect(key1 = showSnackbar) {
            snackbarHostState.showSnackbar(snackbarMessage)
            showSnackbar = false
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RemindAppPreview() {
    ReminderApp()

}