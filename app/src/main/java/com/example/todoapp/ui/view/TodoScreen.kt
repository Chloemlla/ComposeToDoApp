package com.example.todoapp.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.viewmodel.TodoViewModel

@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val todos by viewModel.allTodos.collectAsState()
    var newTask by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        TextField(
            value = newTask,
            onValueChange = { newTask = it },
            placeholder = { Text("Enter new task") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addTodo(newTask)
                newTask = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Task")
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(todos) { todo ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.updateTodo(todo) }
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = todo.title,
                        style = if (todo.isDone)
                            TextStyle(textDecoration = TextDecoration.LineThrough)
                        else
                            TextStyle.Default
                    )
                    IconButton(onClick = { viewModel.deleteTodo(todo) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}