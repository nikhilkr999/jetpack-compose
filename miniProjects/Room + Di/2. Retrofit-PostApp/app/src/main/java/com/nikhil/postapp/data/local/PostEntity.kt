package com.nikhil.postapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts")
data class PostEntity(
    val userId : Int,
    @PrimaryKey(autoGenerate = true)
    val id : Int,
    val title : String,
    val body : String
)
