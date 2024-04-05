package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class ChatResponse(
    @SerializedName("chat")
    val chat: String,
    @SerializedName("chatcreated")
    val chatcreated: String?,
    @SerializedName("source")
    val source: String,
    @SerializedName("sourceavatar")
    val sourceavatar: String?,
    @SerializedName("sourcenick")
    val sourcenick: String?,
    @SerializedName("sourceonline")
    val sourceonline: Boolean?,
    @SerializedName("sourcetoken")
    val sourcetoken: String?,
    @SerializedName("target")
    val target: String,
    @SerializedName("targetavatar")
    val targetavatar: String?,
    @SerializedName("targetnick")
    val targetnick: String?,
    @SerializedName("targetonline")
    val targetonline: Boolean?,
    @SerializedName("targettoken")
    val targettoken: Any?,
    @SerializedName("id")
    val id: String,
    @SerializedName("created")
    val created: String?,
)