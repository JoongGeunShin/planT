package com.example.plant.NaverSearch

import com.google.gson.annotations.SerializedName

data class LocationDTO (
    @SerializedName("items") val locations: List<Location>
)