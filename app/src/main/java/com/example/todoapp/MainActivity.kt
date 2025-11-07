package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.List
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.todoapp.ui.view.EventsScreen
import com.example.todoapp.ui.view.TodoScreen
import com.example.todoapp.ui.viewmodel.EventViewModel
import com.example.todoapp.ui.viewmodel.TodoViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                var showEvents by remember { mutableStateOf(false) }

                // Obtain viewmodels in composition
                val todoViewModel: TodoViewModel = hiltViewModel()
                val eventViewModel: EventViewModel = hiltViewModel()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(if (showEvents) "Events" else "Tasks") },
                            actions = {
                                IconButton(onClick = { showEvents = false }) {
                                    Icon(Icons.Filled.List, contentDescription = "Tasks")
                                }
                                IconButton(onClick = { showEvents = true }) {
                                    Icon(Icons.Filled.Event, contentDescription = "Events")
                                }
                            }
                        )
                    }
                ) { padding ->
                    // show either Events screen or Todo screen
                    if (showEvents) {
                        EventsScreen(viewModel = eventViewModel)
                    } else {
                        TodoScreen(viewModel = todoViewModel)
                    }
                }
            }
        }
    }
}