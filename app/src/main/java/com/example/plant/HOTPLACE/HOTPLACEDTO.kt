package com.example.plant.HOTPLACE

import com.google.gson.annotations.SerializedName

data class HOTPLACEDTO (
    @SerializedName("items") val HOTPLACES: List<HOTPLACE>
)