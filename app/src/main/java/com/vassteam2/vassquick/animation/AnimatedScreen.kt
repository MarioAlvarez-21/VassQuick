import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize


@Composable
fun AnimatedScreen(content: @Composable () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = true) {
        isVisible = true
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        AnimatedVisibility(
            visible = isVisible,
            enter = fadeIn(animationSpec = tween(durationMillis = 500)) + scaleIn(
                initialScale = 0.8f,
                animationSpec = tween(durationMillis = 500)
            ),
            exit = fadeOut(animationSpec = tween(durationMillis = 500)) + scaleOut(
                targetScale = 0.8f,
                animationSpec = tween(durationMillis = 500)
            )
        ) {
            content()
        }
    }
}
