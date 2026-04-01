package com.nikhil.todo.presentation.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.nikhil.todo.presentation.viewModel.TaskViewModel

@Composable
fun TaskScreen(modifier: Modifier = Modifier, viewModel: TaskViewModel = hiltViewModel()) {
    val tasks by viewModel.tasks.collectAsState()
    var text by remember { mutableStateOf("") }

    Column(
        modifier = modifier
    ) {
        Row {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = { Text("Enter Task") },
                modifier = Modifier.weight(1f)
            )

            Button(
                onClick = {
                    if (text.isNotBlank()) {
                        viewModel.insertTask(text)
                        text = ""
                    }
                },
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Text(text = "Add Task")
            }
        }
        Row() {
            if(tasks.size > 0){
                Button(
                    onClick = { viewModel.deleteAllTasks() },

                ) {
                    Text("Delete All")
                }
            }

        }
        LazyColumn {
            items(tasks) { task ->
                TaskItem(task = task, viewModel = viewModel)
            }
        }
    }
}
