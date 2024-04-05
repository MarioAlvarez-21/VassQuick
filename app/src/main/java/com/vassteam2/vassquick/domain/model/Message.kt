package com.vassteam2.vassquick.domain.model

import android.os.Build
import androidx.annotation.RequiresApi
import formatDateToShow


data class Message(
    val chat: String,
    val date: String,
    val id: String,
    val message: String,
    val source: String
) {
    var dateFormatted: String = ""

    init {
        dateFormatted = formatDateToShow(date)
    }

}
