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

class TodoViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: TodoRepository
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

    fun addTodo(title: String) = viewModelScope.launch {
        if (title.isNotBlank()) repository.insert(Todo(title = title))
    }

    fun updateTodo(todo: Todo) = viewModelScope.launch {
        repository.update(todo.copy(isDone = !todo.isDone))
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }
}
