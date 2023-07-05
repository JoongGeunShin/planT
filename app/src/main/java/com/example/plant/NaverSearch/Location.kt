package com.example.plant.NaverSearch

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("title") val title: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("roadAddress") val roadAddress: String
    // 여기서 부터 좌표 위해 setting --> 사용 위해 Integer로 바꿔야됨
)