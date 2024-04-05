package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class ProfileResponse(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("created")
    val created: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("online")
    val online: Boolean,
    @SerializedName("password")
    val password: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("updated")
    val updated: String,
    @SerializedName("uuid")
    val uuid: Any
)