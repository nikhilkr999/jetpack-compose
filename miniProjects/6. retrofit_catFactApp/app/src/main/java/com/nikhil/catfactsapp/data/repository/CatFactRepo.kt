package com.nikhil.catfactsapp.data.repository

import com.nikhil.catfactsapp.data.remote.RetrofitInstance
import com.nikhil.catfactsapp.domain.model.CatFact

class CatFactRepo {

    suspend fun getCatFacts() : CatFact{
        return RetrofitInstance.api.getCatFacts()
    }
}