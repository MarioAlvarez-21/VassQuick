package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class MessagesResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("rows")
    val messageResponses: List<MessageResponse?>?
)