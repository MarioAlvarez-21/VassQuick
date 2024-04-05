package com.vassteam2.vassquick.domain.model

import com.vassteam2.vassquick.di.UserSingleton
import java.time.ZonedDateTime
import java.util.Calendar

data class Chat(
    val id: String,
    val created: String,
    val target: String,
    val source: String,
    val sourceavatar: String? = null,
    val sourcenick: String? = null,
    val sourceonline: Boolean? = null,
    val sourcetoken: String? = null,
    val targetavatar: String? = null,
    val targetnick: String? = null,
    val targetonline: Boolean? = null,
    val targettoken: Any? = null,
    var lastMessage: String? = null,
    var lastMessageDate: String? = null,
    var lastMessageZonedDateTime: ZonedDateTime? = null,
    var lastMessageCalendar: Calendar? = null
) {


    fun getTargetNick(): String {
        return if (UserSingleton.user?.id == source) targetnick ?: "Desconocido"
        else sourcenick ?: "Desconocido"
    }
}