package com.vassteam2.vassquick.presentation.chat.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.White
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun topBar(
    scope: CoroutineScope,
    drawerState: DrawerState
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Black),
        title = {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

                val (title, settings) = createRefs()
                Text(
                    text = stringResource(R.string.title_chat),
                    fontSize = 30.sp,
                    color = Color.White,
                    modifier = Modifier
                        .constrainAs(title) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        })
                IconButton(
                    onClick = { scope.launch { drawerState.open() } },
                    modifier = Modifier.constrainAs(settings) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    })
                {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "menu",
                        modifier = Modifier
                            .size(24.dp),
                        tint = White
                    )
                }
            }
        }
    )
}