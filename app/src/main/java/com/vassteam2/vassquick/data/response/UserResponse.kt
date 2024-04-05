package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("avatar")
    val avatar: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("nick")
    val nick: String,
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("uuid")
    val uuid: String,
    @SerializedName("platform")
    val platform: String,
    @SerializedName("token")
    val token: String,
    @SerializedName("online")
    val online: Boolean,
    @SerializedName("created")
    val created: String,
    @SerializedName("updated")
    val updated: String,
)