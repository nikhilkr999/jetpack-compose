package com.compose.a11jcnotey.screens

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compose.a11jcnotey.roomdb.Note

@Composable
fun DisplayNotesList(
    notes: List<Note>,
    modifier: Modifier = Modifier // ✅ Added this parameter
) {
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        modifier = modifier.fillMaxSize(), // ✅ Apply modifier here
        contentPadding = PaddingValues(16.dp)
    ) {
        items(notes) { note ->
            NoteListItem(note = note)
        }
    }
}
