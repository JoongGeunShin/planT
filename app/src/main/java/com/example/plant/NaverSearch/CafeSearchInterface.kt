package com.example.plant.NaverSearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface CafeSearchInterface {
    @GET("/v1/search/local")
    fun getCafeByLocation(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secretKey: String,
        @Query("query") keyword: String,
        @Query("display") showCounts: Int
    ): Call<LocationDTO>
}