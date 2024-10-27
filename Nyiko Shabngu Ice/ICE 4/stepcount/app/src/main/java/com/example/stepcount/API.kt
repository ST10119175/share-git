package com.example.stepcount

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

class API {
}

data class StepCountApiResponse(
    val id: Int,
    val count: Int
)

interface StepCountApi {
    @GET("stepCountsAPI.json")
    suspend fun getAllStepCounts(): List<StepCountApiResponse>

    @POST("stepCountAPI.json")
    suspend fun addapiStepCount(@Body stepCount: StepCountApiResponse): Response<Unit>
}

object RetrofitInstance {
    private const val BASE_URL = "https://opsc2-2596c-default-rtdb.europe-west1.firebasedatabase.app/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: StepCountApi by lazy {
        retrofit.create(StepCountApi::class.java)
    }
}

