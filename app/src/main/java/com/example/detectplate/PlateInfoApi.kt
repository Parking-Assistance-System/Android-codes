package com.example.detectplate



import okhttp3.MultipartBody

import okhttp3.ResponseBody


import retrofit2.Call
import retrofit2.http.Headers

import retrofit2.http.POST

import retrofit2.http.Multipart
import retrofit2.http.Part


interface PlateInfoApi {

    @Multipart
    @POST("/plate-reader/")
    @Headers(
        "Authorization: Token 1bdb692a4e4f3ce500db34659fa0c702d5587097"
    )
    fun upload(
        @Part file: MultipartBody.Part?
    ): Call<ResponseBody?>?
}