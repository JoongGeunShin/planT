package com.example.plant.NaverGeocode

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.crypto.SecretKey

interface GeocodeInterface {
    @GET("map-geocode/v2/geocode")
    fun getLocationByGeocode(
        @Header("X-NCP-APIGW-API-KEY-ID") id: String,
        @Header("X-NCP-APIGW-API-KEY") secretKey: String,
        @Query("query") address: String
    ) : Call<GeocodeDTO>
}