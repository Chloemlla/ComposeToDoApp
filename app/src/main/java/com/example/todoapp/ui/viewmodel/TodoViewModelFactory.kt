// In com.example.todoapp.ui.viewmodel/TodoViewModelFactory.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.todoapp.repository.SettingsRepository
import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.ui.viewmodel.TodoViewModel

class TodoViewModelFactory(
    private val settingsRepository: SettingsRepository,
    private val todoRepository: TodoRepository // The initialized repository instance
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TodoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            // Pass ONLY the two repository dependencies
            return TodoViewModel(settingsRepository, todoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}