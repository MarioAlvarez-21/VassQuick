package com.vassteam2.vassquick.data.request


import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("login")
    val login: String?,
    @SerializedName("password")
    val password: String?
)