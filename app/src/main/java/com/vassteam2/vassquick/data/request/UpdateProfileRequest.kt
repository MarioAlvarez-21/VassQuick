package com.vassteam2.vassquick.data.request


import com.google.gson.annotations.SerializedName

data class UpdateProfileRequest(
    @SerializedName("nick")
    val nick: String,
    @SerializedName("password")
    val password: String
)