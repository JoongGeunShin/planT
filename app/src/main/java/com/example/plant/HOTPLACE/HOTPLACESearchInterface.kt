package com.example.plant.HOTPLACE

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface HOTPLACESearchInterface {
    @GET("/v1/search/local")
    fun getHOTPLACEByLocation(
        @Header("X-Naver-Client-Id") id: String,
        @Header("X-Naver-Client-Secret") secretKey: String,
        @Query("query") keyword: String,
        @Query("display") display: Int = 10,
        @Query("sort") sort: String = "random"
    ): Call<HOTPLACEDTO>

}