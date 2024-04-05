package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class LogoutResponse(
    @SerializedName("message")
    val message: String
)