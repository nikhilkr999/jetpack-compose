package com.nikhil.todo.presentation.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nikhil.todo.domain.model.Task
import com.nikhil.todo.presentation.viewModel.TaskViewModel

@Composable
fun TaskItem(task: Task, viewModel: TaskViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, color = androidx.compose.ui.graphics.Color.Black)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = task.title,
            modifier = Modifier.weight(1f)
        )

        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = { isChecked ->
                viewModel.updateTask(task.copy(isCompleted = isChecked))
            }
        )
        IconButton(onClick = {
            viewModel.deleteTask(task)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete Task")
        }
    }
}
