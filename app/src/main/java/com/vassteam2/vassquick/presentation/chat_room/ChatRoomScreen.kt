package com.vassteam2.vassquick.presentation.chat_room


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.paging.compose.LazyPagingItems
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.domain.model.Message
import com.vassteam2.vassquick.presentation.chat_room.components.MessageComponent
import com.vassteam2.vassquick.presentation.chat_room.components.PullToRefreshLazyColumn
import com.vassteam2.vassquick.presentation.common.utils.dismissKeyboardAndClearFocus
import com.vassteam2.vassquick.presentation.common.utils.hideKeyboard
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Grey20
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatRoomScreen(
    messages: LazyPagingItems<Message>?,
    state: ChatRoomState,
    onEvent: (ChatRoomEvent) -> Unit,
    navController: NavHostController
) {
    val controller = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = state.personalizedSnackbar.message) {
        state.personalizedSnackbar.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
                onEvent(ChatRoomEvent.ResetMessage)
            }
        }
    }

    LaunchedEffect(key1 = state.isNewMessage) {
        if (state.isNewMessage) {
            messages?.refresh()
            if (controller != null) {
                dismissKeyboardAndClearFocus(
                    keyboardController = controller,
                    focusManager = focusManager
                )
            }
            onEvent(ChatRoomEvent.UpdateIsNewMessage)
        }
    }

    if (messages == null || state.chat == null || state.receivingUser == null) {
        CircularProgress()
    } else {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) {
                    Snackbar(
                        snackbarData = it,
                        contentColor = state.personalizedSnackbar.contentColor,
                        containerColor = state.personalizedSnackbar.containerColor,
                    )
                }
            },
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors().copy(
                        containerColor = Black
                    ),
                    title = {
                        Text(
                            text = state.receivingUser.nick,
                            fontSize = 24.sp,
                            color = Color.White
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Blue40
                            )
                        }
                    },
                )
            },
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = state.newMessage,
                        onValueChange = { onEvent(ChatRoomEvent.ChangeMessage(newMessage = it)) },
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(20.dp),
                        placeholder = { Text(stringResource(R.string.write_a_message)) },
                        colors = TextFieldDefaults.colors().copy(
                            focusedContainerColor = Grey20,
                            unfocusedContainerColor = Grey20,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        )
                    )
                    IconButton(onClick = { onEvent(ChatRoomEvent.SendMessage) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.Send,
                            contentDescription = stringResource(R.string.send_message_icon_button),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        ) {
            Content(
                messages = messages,
                modifier = Modifier
                    .padding(it)
                    .hideKeyboard(keyboardController = controller, focusManager = focusManager),
                idReceivingUser = state.receivingUser.id,
            )
        }
    }
}

@Composable
private fun Content(
    messages: LazyPagingItems<Message>,
    idReceivingUser: String,
    modifier: Modifier = Modifier,
) {
    var isRefreshing by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    PullToRefreshLazyColumn(
        items = messages,
        isRefresing = isRefreshing,
        onRefresh = {
            coroutineScope.launch {
                isRefreshing = true
                messages.refresh()
                delay(1000)
                isRefreshing = false
            }

        },
        content = { index ->
            val message = messages[index]
            message?.let {
                val isFromMe = message.source != idReceivingUser
                MessageComponent(
                    message = message.message,
                    time = message.dateFormatted,
                    isFromMe = isFromMe
                )
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun CircularProgress(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
