package com.vassteam2.vassquick.domain.model


import android.os.Build
import androidx.annotation.RequiresApi
import formatDateToShow
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

data class User(
    val id: String,
    val nick: String,
    val avatar: String? = null,
    val created: String? = null,
    val login: String? = null,
    val online: Boolean? = null,
    val password: String? = null,
    val platform: String? = null,
    val token: String? = null,
    val updated: String? = null,
    val uuid: Any? = null
) {
    val formattedCreatedDate: String?
        get() = created?.let { formatDateToShow(it) }

    val formattedUpdatedDate: String?
        get() = updated?.let { formatDateToShow(it) }


}