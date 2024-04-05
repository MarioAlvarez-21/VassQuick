package com.vassteam2.vassquick.data.request


import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("source")
    val source: String,
    @SerializedName("target")
    val target: String?,

)