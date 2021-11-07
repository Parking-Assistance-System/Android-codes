package com.example.detectplate

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api:PlateInfoApi by lazy{
        Retrofit.Builder()
            .baseUrl("https://api.platerecognizer.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PlateInfoApi::class.java)
    }
}