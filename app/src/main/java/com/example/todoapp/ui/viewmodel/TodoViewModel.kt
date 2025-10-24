package com.example.todoapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.db.TodoDatabase
import com.example.todoapp.data.model.Todo
import com.example.todoapp.repository.TodoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for the To-Do list screen.
 *
 * This class is responsible for preparing and managing the data for the UI.
 * It communicates with the [TodoRepository] to perform CRUD operations on the to-do items.
 *
 * @param application The application that this ViewModel is attached to.
 */
class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository

    /**
     * A [StateFlow] that emits the current list of all to-do items.
     */
    val allTodos: StateFlow<List<Todo>>

    private val _allTodos = MutableStateFlow<List<Todo>>(emptyList())

    init {
        val dao = TodoDatabase.getDatabase(application).todoDao()
        repository = TodoRepository(dao)

        viewModelScope.launch {
            repository.allTodos.collect {
                _allTodos.value = it
            }
        }
        allTodos = _allTodos
    }

    /**
     * Adds a new to-do item to the database.
     *
     * @param title The title of the new to-do item.
     */
    fun addTodo(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) repository.insert(Todo(title = title))
    }

    /**
     * Updates an existing to-do item in the database.
     *
     * This function toggles the [Todo.isDone] property of the given to-do item.
     *
     * @param todo The to-do item to update.
     */
    fun updateTodo(todo: Todo) = viewModelScope.launch {
        repository.update(todo.copy(isDone = !todo.isDone))
    }

    /**
     * Deletes a to-do item from the database.
     *
     * @param todo The to-do item to delete.
     */
    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }

    /**
     * Deletes all completed to-do items from the database.
     */
    fun deleteAllCompleted() {
        viewModelScope.launch {
            allTodos.value.filter { it.isDone }.forEach {
                repository.delete(it)
            }
        }
    }
}
