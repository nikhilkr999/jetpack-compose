package com.compose.a11jcnotey.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int=0,
    val title: String,
    val description: String,
    val color: Int
)
