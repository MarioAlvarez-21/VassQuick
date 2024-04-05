package com.vassteam2.vassquick.data.response

import com.google.gson.annotations.SerializedName

data class DeleteChatResponse (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String? = null
)