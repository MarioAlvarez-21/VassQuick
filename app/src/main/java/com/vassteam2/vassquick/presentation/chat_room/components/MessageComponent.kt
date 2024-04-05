package com.vassteam2.vassquick.presentation.chat_room.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MessageComponent(message: String, time: String, isFromMe: Boolean) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 15.dp, vertical = 6.dp)) {
        BoxWithConstraints(
            modifier = Modifier
                .align(if (isFromMe) Alignment.End else Alignment.Start)
                .clip(
                    RoundedCornerShape(
                        topStart = 20.dp,
                        topEnd = 20.dp,
                        bottomStart = if (isFromMe) 20.dp else 0.dp,
                        bottomEnd = if (isFromMe) 0.dp else 20.dp
                    )
                )
                .background(if (isFromMe) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.onSurface)
                .padding(16.dp)
        ) {
            val maxWidth = with(LocalDensity.current) { constraints.maxWidth.toDp() * 0.75f }
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .widthIn(max = maxWidth)
            ) {
                Column {
                    Text(text = message, color = getTextColor(isFromMe = isFromMe))
                    Text(
                        text = time,
                        fontSize = 12.sp,
                        color = getTextColor(isFromMe = isFromMe),
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

@Composable
private fun getTextColor(isFromMe: Boolean) =
    if (isFromMe) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.surface

