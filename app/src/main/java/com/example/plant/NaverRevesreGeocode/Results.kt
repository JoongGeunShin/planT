package com.example.plant.NaverRevesreGeocode

import com.google.gson.annotations.SerializedName

data class Results (
    @SerializedName("region")
    val region: RegionResponse,
)

data class RegionResponse (
    @SerializedName("area1")
    val area1: AreaResponse,
    @SerializedName("area2")
    val area2: AreaResponse,
    @SerializedName("area3")
    val area3: AreaResponse,
    @SerializedName("area4")
    val area4: AreaResponse
)

data class AreaResponse (
    @SerializedName("name")
    val name: String
)