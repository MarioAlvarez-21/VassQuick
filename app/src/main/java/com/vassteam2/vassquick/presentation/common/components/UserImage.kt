package com.vassteam2.vassquick.presentation.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.ui.theme.Grey40

@Composable
fun UserImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    defaultImageResource: Int = R.drawable.usuario,
    imageSize: Dp = 32.dp
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        modifier = modifier
            .size(imageSize)
            .clip(CircleShape)
            .background(Grey40),
        contentScale = ContentScale.FillBounds,
        error = painterResource(id = defaultImageResource)
    )
}

@Preview
@Composable
fun previewUserImage() {
    UserImage(
        imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/back/132.png",
        contentDescription = "",
        modifier = Modifier
            .height(100.dp)
            .width(100.dp)
    )
}