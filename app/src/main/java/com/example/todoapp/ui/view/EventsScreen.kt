package com.example.todoapp.ui.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.data.model.Event
import com.example.todoapp.ui.viewmodel.EventViewModel
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(viewModel: EventViewModel = hiltViewModel()) {
    val events by viewModel.events.collectAsState()
    val context = LocalContext.current
    var showAdd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Events") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAdd = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Event")
            }
        }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            if (events.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No events yet. Tap + to add an event.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(events, key = { it.id }) { event ->
                        EventRow(
                            event = event,
                            onDelete = { viewModel.deleteEvent(it) },
                            onClick = { /* TODO: implement edit */ },
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }

    if (showAdd) {
        AddEventDialog(
            onDismiss = { showAdd = false },
            onAdd = { title, millis ->
                viewModel.addEvent(title, millis)
                showAdd = false
            }
        )
    }
}

@Composable
fun EventRow(
    event: Event,
    onDelete: (Event) -> Unit,
    onClick: (Event) -> Unit,
    viewModel: EventViewModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = event.title, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(6.dp))
                // Assuming EventViewModel.daysUntil is a companion object function
                val days = EventViewModel.daysUntil(event.dateMillis)
                Text(
                    text = "In $days ${if (days == 1L) "day" else "days"}",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(onClick = { onDelete(event) }) {
                Text("Delete")
            }
        }
    }
}

// The function is now correctly defined at the top level
@Composable
fun AddEventDialog(onDismiss: () -> Unit, onAdd: (String, Long) -> Unit) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var tempDateMillis by remember { mutableStateOf<Long?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                // Use a default time (like current time) if not picked
                val dateMillis = tempDateMillis ?: System.currentTimeMillis()
                if (title.isNotBlank()) onAdd(title.trim(), dateMillis)
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
        title = { Text("Add event") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Event title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        val now = Calendar.getInstance()
                        // 1. Show Date Picker
                        DatePickerDialog(context, { _: DatePicker, y, m, d ->
                            // 2. After Date is picked, show Time Picker
                            val timeNow = Calendar.getInstance()
                            TimePickerDialog(context, { _, hour, minute ->
                                // 3. Combine Date and Time
                                val c = Calendar.getInstance()
                                c.set(Calendar.YEAR, y)
                                c.set(Calendar.MONTH, m)
                                c.set(Calendar.DAY_OF_MONTH, d)
                                c.set(Calendar.HOUR_OF_DAY, hour)
                                c.set(Calendar.MINUTE, minute)
                                c.set(Calendar.SECOND, 0)
                                c.set(Calendar.MILLISECOND, 0)
                                tempDateMillis = c.timeInMillis
                            }, timeNow.get(Calendar.HOUR_OF_DAY), timeNow.get(Calendar.MINUTE), true).show()
                        }, now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)).show()
                    }) {
                        // Display the selected date or a placeholder
                        Text(text = tempDateMillis?.let { java.text.SimpleDateFormat.getDateTimeInstance().format(Date(it)) } ?: "Pick date & time")
                    }
                }
            }
        }
    )
}