package com.example.plant.NaverRevesreGeocode

import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query
import javax.crypto.SecretKey

interface ReverseGeocodeInterface {
    @GET("map-reversegeocode/v2/gc")
    fun getLocationByReverseGeocode(
        @Header("X-NCP-APIGW-API-KEY-ID") id: String,
        @Header("X-NCP-APIGW-API-KEY") secretKey: String,
        @Query("coords") coords: String,
        @Query("orders") orders: String = "roadaddr",
        @Query("output") output: String = "json"
    ) : Call<ReverseGeocodeDTO>
}