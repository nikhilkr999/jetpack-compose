package com.compose.a11jcnotey

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.compose.a11jcnotey.repository.NotesRepository
import com.compose.a11jcnotey.roomdb.NotesDB
import com.compose.a11jcnotey.screens.DisplayDialog
import com.compose.a11jcnotey.screens.DisplayNotesList
import com.compose.a11jcnotey.ui.theme._11JCNoteyTheme
import com.compose.a11jcnotey.viewModel.NoteViewModel
import com.compose.a11jcnotey.viewModel.NoteViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        // Room DB
        val database = NotesDB.getInstance(applicationContext)

        // Repository
        val repository = NotesRepository(database.notesDao)

        // ViewModel Factory
        val viewModelFactory = NoteViewModelFactory(repository)

        // ViewModel
        val noteViewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[NoteViewModel::class.java]


        setContent {
            _11JCNoteyTheme {
                Scaffold(
                    floatingActionButton = { MyFAB(viewModel = noteViewModel) }
                ) { paddingValues ->

                    // Observe LiveData from ViewModel
                    val notes by noteViewModel.allNotes.observeAsState(emptyList())

                    // Display all records from Room DB
                    DisplayNotesList(
                        notes = notes,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }


    }
    @Composable
    fun MyFAB(viewModel: NoteViewModel){

        // Controlling the Dialog Appearance
        var showDialog by remember {
            mutableStateOf(false)
        }

        DisplayDialog(
            viewModel = viewModel,
            showDialog = showDialog) {
            showDialog= false
        }


        FloatingActionButton(
            onClick = {     showDialog = true     },

            containerColor = Color.Blue,
            contentColor = Color.White

        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add Note")

        }

    }
}