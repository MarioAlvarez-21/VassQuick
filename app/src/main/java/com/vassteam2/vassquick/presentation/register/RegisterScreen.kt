package com.vassteam2.vassquick.presentation.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.presentation.common.utils.hideKeyboard
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Red60
import com.vassteam2.vassquick.ui.theme.VassQuickTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable()
fun RegisterScreen(
    navController: NavHostController,
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit
) {
    val controller = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = state.personalizedSnackbar.message) {
        state.personalizedSnackbar.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
                onEvent(RegisterEvent.ResetMessage)
            }
        }
    }

    LaunchedEffect(key1 = state.successRegister) {
        if (state.successRegister) {
            coroutineScope.launch {
                delay(3000)
                navController.navigate(AppScreens.LoginScreen.route)
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
    ) {
        Content(
            state = state,
            onEvent = onEvent,
            navController = navController,
            modifier = Modifier
                .padding(it)
                .hideKeyboard(keyboardController = controller, focusManager = focusManager)
        )
    }
}

@Composable
private fun Content(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    navController: NavHostController,
    modifier: Modifier
) {
    var buttomEnabled by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(scrollState)
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, title, textNick, textPass, textPass2, login, row1, column2) = createRefs()

            Image(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = "",
                modifier = Modifier
                    .size(120.dp)
                    .constrainAs(image) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Text(
                text = stringResource(R.string.register),
                fontSize = 30.sp,
                color = MaterialTheme.colorScheme.onSecondary,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .constrainAs(title) {
                        top.linkTo(image.bottom, margin = 25.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
            )
            NickTextField(state = state, onEvent = onEvent, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .constrainAs(textNick) {
                    top.linkTo(title.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    height = Dimension.wrapContent
                })

            EmailTextField(
                state = state,
                onEvent = onEvent,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .constrainAs(login) {
                        top.linkTo(textNick.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                    })

            PasswordTextField(state = state, onEvent = onEvent, modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp)
                .constrainAs(textPass) {
                    top.linkTo(login.bottom, margin = 5.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

            RepeatedPasswordTextField(
                state = state,
                onEvent = onEvent,
                keyboardController = keyboardController,
                focusManager = focusManager,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .constrainAs(textPass2) {
                        top.linkTo(textPass.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp)
                    .constrainAs(row1) {
                        top.linkTo(textPass2.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)

                    }
            ) {
                Checkbox(
                    checked = buttomEnabled,
                    onCheckedChange = {
                        buttomEnabled = it
                    }
                )
                Text(
                    text = stringResource(R.string.i_accept),
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(end = 5.dp)
                )
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.the_terms_of_the_policy_and_privacy)),
                    style = TextStyle(color = Blue40, fontSize = 15.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    onClick = {
                        navController.navigate(AppScreens.ConditionsScreen.route)
                    }
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        start = 15.dp, end = 15.dp, bottom = 20.dp,
                        top = 20.dp
                    )
                    .constrainAs(column2) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(row1.bottom)
                    }
            ) {
                Button(
                    onClick = {
                        onEvent(RegisterEvent.ClickRegister)
                    },
                    enabled = buttomEnabled,
                    shape = RoundedCornerShape(20),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .height(56.dp)
                ) {
                    Text(stringResource(R.string.register_register), fontSize = 17.sp)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.already_have_an_account_register),
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                    TextButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Text(text = stringResource(R.string.back))
                    }
                }
            }
        }
    }
}

@Composable
private fun RepeatedPasswordTextField(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }
    TextField(
        value = state.repeatPassword,
        onValueChange = { newValue ->
            onEvent(RegisterEvent.ChangeRepeatPassword(newRepeatPassword = newValue))
        },
        supportingText = {
            Text(text = state.messageRepeatPassword, color = Red60)
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        ),
        label = { Text(stringResource(R.string.repeat_password)) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = stringResource(id = R.string.password),
                tint = if (state.isValidRepeatPassword) Red60 else Blue40
            )
        },
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Grey20,
            focusedContainerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent
        ),
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) stringResource(id = R.string.hide_password) else stringResource(
                        id = R.string.show_password
                    )
                )
            }
        },
        isError = state.isValidRepeatPassword,
        modifier = modifier
    )
}

@Composable
private fun PasswordTextField(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = state.password,
        supportingText = {
            Text(text = state.messagePassword, color = Red60)
        },
        onValueChange = { newValue ->
            onEvent(RegisterEvent.ChangePassword(newPassword = newValue))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = stringResource(id = R.string.password),
                tint = if (state.isValidPassword) Red60 else Blue40
            )
        },
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Grey20,
            unfocusedContainerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent
        ),
        label = { Text(stringResource(R.string.password_register)) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
                )
            }
        },
        isError = state.isValidPassword,
        modifier = modifier
    )
}

@Composable
private fun EmailTextField(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = state.login,
        onValueChange = { newValue ->
            onEvent(RegisterEvent.ChangeLogin(newLogin = newValue))
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AlternateEmail,
                contentDescription = stringResource(id = R.string.login),
                tint = if (state.isValidLogin) Red60 else Blue40
            )
        },
        shape = RoundedCornerShape(20),
        label = { Text(stringResource(R.string.email_register)) },
        supportingText = {
            Text(text = state.messageLogin, color = Red60)
        },
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Grey20,
            unfocusedContainerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent

        ),
        isError = state.isValidLogin,
        modifier = modifier
    )
}

@Composable
private fun NickTextField(
    state: RegisterState,
    onEvent: (RegisterEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = state.nick,
        onValueChange = { newValue ->
            onEvent(RegisterEvent.ChangeNick(newNick = newValue))
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "seach",
                tint = if (state.isValidNick) Red60 else Blue40
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        label = { Text(stringResource(R.string.nick)) },
        shape = RoundedCornerShape(20),
        supportingText = {
            Text(text = state.messageNick, color = Red60)
        },
        isError = state.isValidNick,
        colors = TextFieldDefaults.colors().copy(
            focusedContainerColor = Grey20,
            unfocusedContainerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent

        ),
        modifier = modifier

    )
}

@Preview
@Composable
fun preview() {
    VassQuickTheme {
        RegisterScreen(
            navController = rememberNavController(),
            state = RegisterState(),
            onEvent = { })
    }
}