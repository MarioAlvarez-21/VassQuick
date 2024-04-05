package com.vassteam2.vassquick.presentation.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.di.UserSingleton.user
import com.vassteam2.vassquick.domain.constants.HAVE_BIOMETRIC
import com.vassteam2.vassquick.domain.util.Storage
import com.vassteam2.vassquick.domain.util.hasBiometricCapability
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Blue80
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Grey40
import com.vassteam2.vassquick.ui.theme.Grey60
import com.vassteam2.vassquick.ui.theme.Red60
import com.vassteam2.vassquick.ui.theme.White
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavHostController,
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit

) {
    val context = LocalContext.current
    var editEnabled by remember { mutableStateOf(false) }
    var bottomEnabled by remember { mutableStateOf(false) }
    var checkBiometric by remember { mutableStateOf(Storage.contain(context, HAVE_BIOMETRIC)) }
    val keyboardController: SoftwareKeyboardController? = null
    val focusManager: FocusManager = LocalFocusManager.current
    val scrollState = rememberScrollState()
    val avatarResource = if (!user.avatar.isNullOrEmpty()) {
        painterResource(
            id = user.avatar!!.toIntOrNull() ?: R.drawable.usuario
        )
    } else {
        painterResource(id = R.drawable.usuario)
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.personalizedSnackbar.message) {
        state.personalizedSnackbar.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
                onEvent(ProfileEvent.ResetMessage)
            }
        }
    }

    LaunchedEffect(key1 = state.successUpdate) {
        if (state.successUpdate) {
            coroutineScope.launch {
                bottomEnabled = false
                editEnabled = false
            }
        }
    }
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
                    ConstraintLayout(Modifier.fillMaxWidth()) {

                        val (title, editIcon) = createRefs()
                        Text(
                            text = stringResource(R.string.perfil),
                            fontSize = 25.sp,
                            color = Color.White,
                            modifier = Modifier
                                .constrainAs(title) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    bottom.linkTo(parent.bottom)
                                })
                        IconButton(onClick = { editEnabled = !editEnabled },
                            modifier = Modifier.constrainAs(editIcon) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = stringResource(id = R.string.back),
                                tint = Blue40
                            )
                        }

                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                            tint = Blue40
                        )
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .background(Black)
                .verticalScroll(scrollState)
        ) {

            ConstraintLayout(Modifier.fillMaxSize()) {

                val (avatar, textLogin, textFieldNick, textFieldPass, button, plataform,
                    created, updated, rowBiometric) = createRefs()
                Image(
                    painter = avatarResource,
                    contentDescription = "Avatar",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape)
                        .background(Grey40)
                        .constrainAs(avatar) {
                            top.linkTo(parent.top, 25.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
                if (state.user == null) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                } else {

                    LaunchedEffect(key1 = state.user.password, key2 = state.user.nick) {
                        bottomEnabled =
                            state.user.nick != state.userCopy?.nick || state.user.password != state.userCopy.password
                    }

                    Text(text = state.user.login ?: "", fontSize = 25.sp, modifier = Modifier
                        .padding(top = 50.dp)
                        .constrainAs(textLogin) {
                            top.linkTo(avatar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    NickTextField(
                        onEvent = onEvent,
                        editEnabled = editEnabled,
                        state = state,
                        modifier = Modifier.constrainAs(textFieldNick) {
                            top.linkTo(textLogin.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    PasswordTextField(
                        state = state,
                        onEvent = onEvent,
                        keyboardController = keyboardController,
                        focusManager = focusManager,
                        editEnabled = editEnabled,
                        modifier = Modifier.constrainAs(textFieldPass) {
                            top.linkTo(textFieldNick.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .constrainAs(rowBiometric) {
                                top.linkTo(textFieldPass.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                    ) {
                        Text(
                            text = stringResource(R.string.activate_biometrics),
                            color = Color.White,
                            modifier = Modifier
                                .padding(start = 20.dp)
                                .weight(1f)
                        )
                        Switch(
                            checked = checkBiometric,
                            onCheckedChange = { checked ->
                                checkBiometric = checked
                                onEvent(ProfileEvent.CheckBiometric(checked))
                            },
                            enabled = hasBiometricCapability(context),
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                                .padding(end = 15.dp)
                        )
                    }

                    Text(
                        text = stringResource(R.string.created) + (state.user.formattedCreatedDate
                            ?: stringResource(R.string.not_available)),
                        textAlign = TextAlign.Center,
                        color = White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp, start = 15.dp, end = 15.dp)
                            .constrainAs(created) {
                                top.linkTo(rowBiometric.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            })
                    Text(
                        text = stringResource(R.string.platform) + (state.user.platform
                            ?: stringResource(R.string.not_available)),
                        textAlign = TextAlign.Center,
                        color = White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                            .constrainAs(plataform) {
                                top.linkTo(created.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            })
                    Text(
                        text = stringResource(R.string.update) + (state.user.formattedUpdatedDate
                            ?: stringResource(R.string.not_available)),
                        textAlign = TextAlign.Center,
                        color = White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, start = 15.dp, end = 15.dp)
                            .constrainAs(updated) {
                                top.linkTo(plataform.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            })
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(
                                start = 15.dp, end = 15.dp, bottom = 20.dp,
                                top = 20.dp
                            )
                            .constrainAs(button) {
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                top.linkTo(updated.bottom)
                            }
                    ) {
                        Button(
                            onClick = { onEvent(ProfileEvent.ClickSaveUpdate) },
                            enabled = bottomEnabled,
                            shape = RoundedCornerShape(20),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp)
                                .height(56.dp)
                        ) {
                            Text(stringResource(R.string.update), fontSize = 17.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NickTextField(
    onEvent: (ProfileEvent) -> Unit,
    editEnabled: Boolean,
    state: ProfileState,
    modifier: Modifier = Modifier
) {
    TextField(
        value = state.user?.nick ?: "",
        onValueChange = { newNick ->
            onEvent(ProfileEvent.ChangeNick(newNick))
        },
        enabled = editEnabled,
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "nick",
                tint = if (state.invalideNick) Red60 else Blue40
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text("Nick", color = Grey60) },
        shape = RoundedCornerShape(20),
        isError = state.invalideNick,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Grey20,
            unfocusedContainerColor = Grey20,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledTextColor = Blue80,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent
        ),
        supportingText = {
            Text(text = state.messageNick, color = Red60)
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 20.dp, start = 15.dp, end = 15.dp)

    )
}


@Composable
private fun PasswordTextField(
    state: ProfileState,
    onEvent: (ProfileEvent) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    editEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var mutablePassword by remember { mutableStateOf(state.user?.password) }
    TextField(
        value = mutablePassword!!,
        supportingText = {
            Text(text = state.messagePassword, color = Red60)
        },
        onValueChange = { newPass ->
            mutablePassword = newPass
            onEvent(ProfileEvent.ChangePassword(mutablePassword!!))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        ),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = "password",
                tint = if (state.invalidePassword) Red60 else Blue40
            )
        },
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Grey20,
            unfocusedContainerColor = Grey20,
            disabledContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            disabledTextColor = Blue80,
            errorIndicatorColor = Color.Transparent
        ),
        label = { Text(stringResource(R.string.password), color = Grey60) },
        enabled = editEnabled,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )
                )
            }
        },
        isError = state.invalidePassword,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 15.dp, end = 15.dp)


    )
}