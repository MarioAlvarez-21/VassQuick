package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("user")
    val userResponse: UserResponse
)
