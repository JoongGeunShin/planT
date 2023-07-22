package com.example.plant.NaverGeocode

import com.google.gson.annotations.SerializedName

data class GeocodeDTO(
    @SerializedName("addresses") val addresses: List<Addresses>
)
