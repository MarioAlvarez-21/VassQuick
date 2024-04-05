package com.vassteam2.vassquick.presentation.chat

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.domain.model.Chat
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.presentation.chat.components.MessageCard
import com.vassteam2.vassquick.presentation.chat.components.PullToRefreshLazyColumn
import com.vassteam2.vassquick.presentation.chat.components.SearchBar
import com.vassteam2.vassquick.presentation.chat.components.topBar
import com.vassteam2.vassquick.presentation.chat.menu.DrawerContent
import com.vassteam2.vassquick.presentation.common.utils.hideKeyboard
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue40
import kotlinx.coroutines.launch

@Composable
fun ChatScreen(
    navController: NavHostController,
    state: ChatState,
    filteredChats: State<List<Chat>>,
    onEvent: (ChatEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(state.navigateToLogin) {
        if (state.navigateToLogin) {
            navController.navigate(AppScreens.LoginScreen.route) {
                popUpTo(AppScreens.ChatScreen.route) { inclusive = true }
            }
        }
    }

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    onEvent(ChatEvent.UpdateChatsOnlineStatus(true))
                }

                Lifecycle.Event.ON_PAUSE -> {
                    onEvent(ChatEvent.UpdateChatsOnlineStatus(false))
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    BackHandler(enabled = true) {
        (context as? Activity)?.finish()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                onProfileClicked = {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.ProfileScreen.route)
                },
                onProfileEditClicked = {
                    scope.launch { drawerState.close() }
                    navController.navigate(AppScreens.ProfileEditScreen.route)
                },

                onLogoutClicked = {
                    scope.launch { drawerState.close() }
                    onEvent(ChatEvent.ShowLogoutConfirmation)
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                Column(verticalArrangement = Arrangement.spacedBy((-1).dp)) {
                    topBar(scope = scope, drawerState = drawerState)
                    SearchBar(
                        onEvent = onEvent,
                        keyboardController = keyboardController,
                        focusManager = focusManager,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 15.dp, end = 15.dp, top = 10.dp)
                    )
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(AppScreens.UsersScreen.route) },
                    containerColor = Blue40,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", tint = Black)
                }
            },
            floatingActionButtonPosition = FabPosition.End,
            modifier = Modifier.hideKeyboard(
                keyboardController = keyboardController,
                focusManager = focusManager
            )
        ) { innerPadding ->
            Content(
                onEvent = onEvent,
                state = state,
                filteredChats = filteredChats,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }

    if (state.showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text(stringResource(R.string.dialog_logout)) },
            text = { Text(stringResource(R.string.dialog_close_session)) },
            confirmButton = {
                Button(onClick = { onEvent(ChatEvent.ConfirmLogout) }) {
                    Text(stringResource(R.string.dialog_confirm))
                }
            },
            dismissButton = {
                Button(onClick = { onEvent(ChatEvent.DismissLogoutConfirmation) }) {
                    Text(stringResource(R.string.dialog_cancel))
                }
            }
        )
    }
}

@Composable
private fun Content(
    onEvent: (ChatEvent) -> Unit,
    state: ChatState,
    filteredChats: State<List<Chat>>,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    LaunchedEffect(key1 = state.isRefreshing) {
        if (!state.isRefreshing) {
            listState.scrollToItem(index = 0)
        }
    }
    BackHandler {
        onEvent(ChatEvent.ClearSearchAndReloadChats)
    }
    PullToRefreshLazyColumn(
        items = filteredChats.value,
        isRefreshing = state.isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                onEvent(ChatEvent.Refresh)
            }

        },
        content = { index ->
            val chat = filteredChats.value[index]
            MessageCard(chat = chat, onEvent = onEvent, navController = navController)
        },
        modifier = modifier
            .fillMaxWidth()
            .fillMaxSize(),
        lazyListState = listState
    )
    if (state.showDeleteDialog && state.selectedChatToDelete != null) {
        ShowDeleteChatConfirmation(
            onConfirm = {
                onEvent(ChatEvent.ConfirmDeleteChat(chat = state.selectedChatToDelete!!))
            },
            onCancel = { onEvent(ChatEvent.CancelDeleteChat) }
        )
    }
}


@Composable
fun ShowDeleteChatConfirmation(onConfirm: () -> Unit, onCancel: () -> Unit) {
    AlertDialog(
        onDismissRequest = { },
        title = { Text(stringResource(R.string.delete_chat)) },
        text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_chat)) },
        confirmButton = {
            Button(onClick = onConfirm) { Text(stringResource(R.string.confirm)) }
        },
        dismissButton = {
            Button(onClick = onCancel) { Text(stringResource(R.string.cancel)) }
        }
    )
}