package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class BiometricResponse(
    @SerializedName("token")
    val token: String,
    @SerializedName("user")
    val userResponse: UserResponse
)
