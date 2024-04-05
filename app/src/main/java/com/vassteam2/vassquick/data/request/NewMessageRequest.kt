package com.vassteam2.vassquick.data.request


import com.google.gson.annotations.SerializedName

data class NewMessageRequest(
    @SerializedName("chat")
    val chat: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("source")
    val source: String?
)