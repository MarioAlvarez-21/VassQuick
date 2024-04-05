package com.vassteam2.vassquick.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.vassteam2.vassquick.data.response.MessageResponse
import com.vassteam2.vassquick.domain.model.Message

@RequiresApi(Build.VERSION_CODES.O)
fun MessageResponse.toMessage(): Message {
    return Message(
        chat = this.chat!!,
        date = this.date!!,
        message = this.message!!,
        id = this.id!!,
        source = this.source!!
    )
}