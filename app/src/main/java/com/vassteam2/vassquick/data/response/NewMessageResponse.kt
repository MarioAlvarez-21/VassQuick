package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class NewMessageResponse(
    @SerializedName("success")
    val success: Boolean
)