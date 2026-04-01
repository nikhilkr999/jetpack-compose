package com.nikhil.randomuser.model

import com.google.gson.annotations.SerializedName

data class ResponseData(
    val results : List<User>
)

data class User(
    val name : Name,
    val email : String,
    val picture : Picture
)

data class Name(
    @SerializedName("first")
    val firstName : String,
    @SerializedName("last")
    val lastName : String
)

data class Picture(
    val large : String
)
