package com.vassteam2.vassquick.domain.model

import com.google.gson.annotations.SerializedName

data class NewMessage(
    val chat: String,
    val message: String,
    val source: String
)
