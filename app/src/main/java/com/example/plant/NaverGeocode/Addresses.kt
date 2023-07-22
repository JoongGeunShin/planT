package com.example.plant.NaverGeocode

import com.google.gson.annotations.SerializedName

data class Addresses(
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("jibunAddress") val jibunAddress: String,
    @SerializedName("x") val x: String, // 경도
    @SerializedName("y") val y: String  // 위도
)

