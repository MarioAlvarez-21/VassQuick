package com.vassteam2.vassquick.data.response


import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("success")
    val success: String
)