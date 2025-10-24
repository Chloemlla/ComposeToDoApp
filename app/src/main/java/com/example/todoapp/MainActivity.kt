package com.example.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todoapp.ui.view.TodoScreen
import com.example.todoapp.ui.viewmodel.TodoViewModel

/**
 * The main activity of the To-Do application.
 *
 * This activity is the entry point of the application and hosts the [TodoScreen] composable.
 */
class MainActivity : ComponentActivity() {
    /**
     * Called when the activity is first created.
     *
     * This method sets up the content of the activity, including the theme and the main screen.t
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     * this Bundle contains the data it most recently supplied in [onSaveInstanceState].
     * Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                // Use viewModel with factory for AndroidViewModel
                val viewModel: TodoViewModel = viewModel(
                    factory = androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
                        .getInstance(application)
                )
                TodoScreen(viewModel)
            }
        }
    }
}
