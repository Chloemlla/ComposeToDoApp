package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Todo
import com.example.todoapp.repository.SettingsRepository
import com.example.todoapp.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {

    // --- Core To-Do Logic ---

    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos

    init {
        viewModelScope.launch {
            todoRepository.allTodos.collect {
                _allTodos.value = it
            }
        }
    }

    /**
     * Adds a new to-do item to the database.
     *
     * @param title The title of the new to-do item.
     */
    fun addTodo(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) {
            todoRepository.insert(Todo(title = title))
        }
    }

    /**
     * Updates an existing to-do item in the database.
     *
     * This function toggles the [Todo.isDone] property of the given to-do item.
     *
     * @param todo The to-do item to update.
     */
    fun updateTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.update(todo.copy(isDone = !todo.isDone))
    }

    /**
     * Restores a deleted to-do item with its original ID and properties.
     * Uses REPLACE conflict strategy to restore the exact same record.
     *
     * @param todo The original to-do item to restore.
     */
    fun restoreTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.insert(todo)
    }

    /**
     * Deletes a to-do item from the database.
     *
     * @param todo The to-do item to delete.
     */
    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.delete(todo)
    }
}