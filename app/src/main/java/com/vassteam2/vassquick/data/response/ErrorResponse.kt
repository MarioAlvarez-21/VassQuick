package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("message")
    val message:String,
    @SerializedName("success")
    val success:String
)
