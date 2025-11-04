package com.example.todoapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.model.Todo
import com.example.todoapp.repository.SettingsRepository
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// 1. Changed inheritance to ViewModel (not AndroidViewModel)
class TodoViewModel(
    // 2. Injected dependencies via the custom factory
    private val settingsRepository: SettingsRepository,
    private val todoRepository: TodoRepository
) : ViewModel() {


    // --- Core To-Do Logic ---

    // Using MutableStateFlow/StateFlow for simplicity, though Flow<List<Todo>> is also common.
    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())
    val allTodos: StateFlow<List<Todo>> = _allTodos

    init {
        viewModelScope.launch {
            // Use the injected todoRepository
            todoRepository.allTodos.collect {
                _allTodos.value = it
            }
        }
    }

    // All your existing CRUD functions (addTodo, updateTodo, deleteTodo, etc.) remain the same,
    // simply using 'todoRepository.' instead of the old 'repository.'
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

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        todoRepository.delete(todo)
    }

    // ...
}