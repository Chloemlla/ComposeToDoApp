package com.example.todoapp

import TodoViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.view.TodoScreen
import com.example.todoapp.ui.viewmodel.TodoViewModel
// IMPORTANT: Make sure this file exists and contains the Context.dataStore extension
import com.example.todoapp.data.dataStore
import com.example.todoapp.data.db.TodoDatabase
import com.example.todoapp.repository.SettingsRepository
import com.example.todoapp.repository.TodoRepository


class MainActivity : ComponentActivity() {

    // We keep the settingsRepository lazy for a clean split
    private val settingsRepository by lazy {
        SettingsRepository(this.dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Initialize the TodoRepository here using the applicationContext
        val todoDao = TodoDatabase.getDatabase(applicationContext).todoDao()
        val todoRepository = TodoRepository(todoDao)

        // 2. Create the custom factory
        val factory = TodoViewModelFactory(
            settingsRepository = settingsRepository,
            todoRepository = todoRepository
        )

        setContent {
            MaterialTheme {
                // 3. Use your custom factory
                val viewModel: TodoViewModel = viewModel(
                    factory = factory
                )
                TodoScreen(viewModel)
            }
        }
    }
}