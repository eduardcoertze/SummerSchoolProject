package com.example.summerschool.network

import com.example.summerschool.models.NasaResponse
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Query

interface NasaService {
    @GET("apod")
    fun getData(
        @Query("date") date: String,
        @Query("api_key") api_key: String
    ) : Call<NasaResponse>

}