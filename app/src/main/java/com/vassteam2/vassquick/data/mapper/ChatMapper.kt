package com.vassteam2.vassquick.data.mapper

import com.vassteam2.vassquick.data.response.ChatResponse
import com.vassteam2.vassquick.domain.model.Chat

fun ChatResponse.toChat(): Chat {
    return Chat(
        id = if(this.id.isNullOrEmpty()) this.chat else this.id,
        source = this.source,
        sourceavatar = this.sourceavatar,
        sourcenick = this.sourcenick,
        sourceonline = this.sourceonline,
        sourcetoken = this.sourcetoken,
        target = this.target,
        targetavatar = this.targetavatar,
        targetnick = this.targetnick,
        targetonline = this.targetonline,
        targettoken = this.targettoken,
        created = if(this.created.isNullOrEmpty()) this.chatcreated!! else this.created
    )
}