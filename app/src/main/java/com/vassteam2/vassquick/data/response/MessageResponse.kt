package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("chat")
    val chat: String?,
    @SerializedName("date")
    val date: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("message")
    val message: String?,
    @SerializedName("source")
    val source: String?
)