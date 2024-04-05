package com.vassteam2.vassquick.presentation.chat.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow.Companion.Ellipsis
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.presentation.chat.ChatEvent
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Grey40
import com.vassteam2.vassquick.ui.theme.White

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageCard(
    chat: Chat,
    onEvent: (ChatEvent) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val avatarResource = if (!UserSingleton.user.avatar.isNullOrEmpty()) {
        painterResource(
            id = UserSingleton.user.avatar!!.toIntOrNull() ?: R.drawable.usuario
        )
    } else {
        painterResource(id = R.drawable.usuario)
    }

    ListItem(
        headlineContent = {
            Text(
                text = chat.getTargetNick(),
                color = White,
                fontSize = 20.sp,
                textAlign = TextAlign.Start, maxLines = 1,
                overflow = Ellipsis,
                modifier = modifier
            )
        },
        supportingContent = {
            Text(
                text = chat.lastMessage ?: stringResource(R.string.there_are_no_pending_messages),
                fontSize = 16.sp, color = Grey40,
                maxLines = 1,
                overflow = Ellipsis,
                modifier = Modifier
            )
        },
        leadingContent = {
            Box {
                Image(
                    painter = avatarResource,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Grey40)
                        .padding(4.dp),
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDownCircle,
                    contentDescription = "Online Status",
                    tint = if (chat.targetonline == true) Color.Green else Color.Red,
                    modifier = Modifier
                        .size(22.dp)
                        .padding(top = 8.dp)
                        .align(Alignment.BottomEnd)
                )
            }

        },
        trailingContent = {
            Text(
                text = chat.lastMessageDate ?: stringResource(R.string.unknown_date),
                color = Grey40,
                fontSize = 12.sp,
                textAlign = TextAlign.End,
            )
        },
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight(unbounded = true)
            .combinedClickable(
                onClick = { navController.navigate(AppScreens.ChatRoomScreen.route + "/" + chat.id) },
                onLongClick = { onEvent(ChatEvent.OnLongItemClick(chat)) },
            ),
        colors = ListItemDefaults.colors(
            containerColor = Color.Black
        )
    )
}