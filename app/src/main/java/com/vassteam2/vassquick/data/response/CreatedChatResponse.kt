package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class CreatedChatResponse(
    @SerializedName("chat")
    val chat: ChatResponse,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("created")
    val created: Boolean,
)
