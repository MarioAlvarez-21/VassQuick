package com.vassteam2.vassquick.presentation.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.ui.theme.Black
import kotlinx.coroutines.delay

@Composable
fun VassQuickSplash(navController: NavController) {

    val opacity = remember { Animatable(0f) }
    val scale = remember { Animatable(0.5f) }

    LaunchedEffect(key1 = true) {
        opacity.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 500)
        )
        delay(500)
        navController.navigate("main_screen") {
            popUpTo("splash_screen") { inclusive = true }
        }
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Black)
    ) {
            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(R.string.app_logo),
                modifier = Modifier.size(100.dp)
                    .graphicsLayer {
                        alpha = opacity.value
                        scaleX = scale.value
                        scaleX = scale.value
                    }
            )
        }
    }
