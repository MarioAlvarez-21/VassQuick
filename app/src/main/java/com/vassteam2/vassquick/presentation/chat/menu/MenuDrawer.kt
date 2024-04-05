package com.vassteam2.vassquick.presentation.chat.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Grey40
import com.vassteam2.vassquick.ui.theme.White

@Composable
fun DrawerContent(
    onProfileClicked: () -> Unit,
    onProfileEditClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Black)
            .padding(16.dp)
            .width(250.dp)
            .fillMaxHeight()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(bottom = 24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                val avatarResource = if (UserSingleton.user.avatar?.isNotEmpty() == true) {
                    painterResource(
                        id = UserSingleton.user.avatar?.toIntOrNull() ?: R.drawable.usuario
                    )
                } else {
                    painterResource(id = R.drawable.usuario)
                }

                Image(
                    painter = avatarResource,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Grey40)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = UserSingleton.user.nick,
                    color = White,
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Botones del Drawer
        DrawerButton(icon = Icons.Default.Person, label = stringResource(R.string.drawer_profile), onClick = onProfileClicked)
        Spacer(modifier = Modifier.height(16.dp))
        DrawerButton(
            icon = Icons.AutoMirrored.Filled.Logout,
            label = stringResource(R.string.drawer_close_session),
            onClick = onLogoutClicked
        )
    }
}

@Composable
fun DrawerButton(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Blue40,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            color = White,
            fontSize = 16.sp
        )
    }
    Divider(color = Blue40, thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
}