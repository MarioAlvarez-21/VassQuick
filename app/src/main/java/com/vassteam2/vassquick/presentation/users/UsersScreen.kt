package com.vassteam2.vassquick.presentation.users

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDownCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.domain.model.User
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.presentation.common.utils.hideKeyboard
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Green
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Grey40
import com.vassteam2.vassquick.ui.theme.Red40
import com.vassteam2.vassquick.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun UsersScreen(
    navController: NavHostController,
    state: UsersState,
    onEvent: (UsersEvent) -> Unit
) {
    val lazyColumnListState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var searchText by remember { mutableStateOf("") }
    val filteredUsers = state.users.filter { user ->
        user.nick.contains(searchText, ignoreCase = true)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .hideKeyboard(keyboardController = keyboardController, focusManager = focusManager)
    ) {
        val (toolbar, search, lazyColumn) = createRefs()

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Black),
            title = {
                ConstraintLayout {

                    val (title, sizeList) = createRefs()
                    Text(
                        text = stringResource(R.string.users),
                        fontSize = 25.sp,
                        color = Color.White,
                        modifier = Modifier
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            })

                    Text(
                        text = stringResource(R.string.users_size, filteredUsers.size),
                        fontSize = 15.sp,
                        color = White,
                        modifier = Modifier
                            .constrainAs(sizeList) {
                                top.linkTo(title.bottom)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            }
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Blue40
                    )
                }
            },
            modifier = Modifier.constrainAs(toolbar) {}
        )
        if (keyboardController != null) {
            TextField(
                value = searchText,
                onValueChange = { newText ->
                    searchText = newText
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController.hide()
                        focusManager.clearFocus()
                    },
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search_icon),
                        tint = Blue40
                    )
                },
                label = {
                    Text(
                        text = stringResource(R.string.search_user)
                    )
                },
                shape = RoundedCornerShape(20),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Grey20,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 15.dp, end = 15.dp, top = 10.dp)
                    .constrainAs(search) {
                        start.linkTo(parent.start)
                        top.linkTo(toolbar.bottom)
                        end.linkTo(parent.end)
                    },
            )
            LazyColumn(
                state = lazyColumnListState,
                contentPadding = PaddingValues(horizontal = 10.dp),
                modifier = Modifier
                    .constrainAs(lazyColumn) {
                        start.linkTo(parent.start)
                        top.linkTo(search.bottom)
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                        height = Dimension.fillToConstraints

                    }
            ) {
                items(filteredUsers) { user ->
                    cardPreviewConstraint(
                        user = user,
                        state = state,
                        onEvent = onEvent,
                        navController = navController
                    )
                }
            }
        }
    }

}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun cardPreviewConstraint(
    user: User,
    state: UsersState,
    onEvent: (UsersEvent) -> Unit,
    navController: NavHostController
) {

    var color by remember { mutableStateOf(Color.Red) }

    LaunchedEffect(key1 = state.isLoading) {
        if (state.isLoading) {
            onEvent(UsersEvent.ResetLoading(state))
            navController.navigate(AppScreens.ChatRoomScreen.route + "/${state.idChat}") {
                popUpTo(AppScreens.ChatScreen.route) {
                    inclusive = true
                }
                launchSingleTop = true
            }
        }
    }



    Card(
        colors = CardDefaults.cardColors(Black),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .wrapContentHeight(unbounded = true)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = {
                        onEvent(UsersEvent.ClickUser(user))

                    },
                    onLongClick = { },
                )
        ) {
            val (image, text, spacer, icon) = createRefs()

            val avatarResource = if (!user.avatar.isNullOrEmpty()) {
                painterResource(
                    id = user.avatar.toIntOrNull() ?: R.drawable.usuario
                )
            } else {
                painterResource(id = R.drawable.usuario)
            }
            Image(
                painter = avatarResource,
                contentDescription = "Avatar",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Grey40)
                    .padding(4.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        bottom.linkTo(parent.bottom)
                    },
            )
            color = if (user.online == true) {
                Green
            } else {
                Red40
            }
            Icon(
                imageVector = Icons.Default.ArrowDropDownCircle,
                contentDescription = "Online Status",
                tint = color,
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 8.dp)
                    .constrainAs(icon) {
                        bottom.linkTo(image.bottom)
                        start.linkTo(image.end, (-10).dp)
                    }
            )
            Text(
                text = user.nick,
                color = White,
                fontSize = 18.sp,
                textAlign = TextAlign.Start,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(text) {
                        top.linkTo(parent.top)
                        start.linkTo(image.end, margin = 10.dp)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, margin = 8.dp)
                        width = Dimension.fillToConstraints
                    })

        }
    }

}


