package com.example.plant.NaverRevesreGeocode

import com.google.gson.annotations.SerializedName


data class ReverseGeocodeDTO(
    @SerializedName("results") val results: List<Results>
)



