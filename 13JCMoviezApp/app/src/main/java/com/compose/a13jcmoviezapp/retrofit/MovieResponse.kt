package com.compose.a13jcmoviezapp.retrofit

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    val pages: Int,
    val results : List<Movie>,

    @SerializedName("total_pages")   //can be written to customiz name
    val totalPages: Int,

    val total_results: Int

)
