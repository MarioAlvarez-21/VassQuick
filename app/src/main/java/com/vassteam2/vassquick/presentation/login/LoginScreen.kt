package com.vassteam2.vassquick.presentation.login

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.domain.constants.HAVE_BIOMETRIC
import com.vassteam2.vassquick.domain.constants.LOGIN
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import com.vassteam2.vassquick.domain.constants.TRUE
import com.vassteam2.vassquick.domain.util.SecureStorage
import com.vassteam2.vassquick.domain.util.Storage
import com.vassteam2.vassquick.domain.util.hasBiometricCapability
import com.vassteam2.vassquick.domain.util.showBiometricPrompt
import com.vassteam2.vassquick.navigation.AppScreens
import com.vassteam2.vassquick.presentation.common.utils.hideKeyboard
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Red60
import com.vassteam2.vassquick.ui.theme.VassQuickTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navController: NavHostController, state: LoginState, onEvent: (LoginEvent) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var doubleBackToExitPressedOnce by remember { mutableStateOf(false) }
    val controller = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(key1 = state.personalizedSnackbar.message) {
        state.personalizedSnackbar.message?.let {
            scope.launch {
                snackbarHostState.showSnackbar(message = it)
                onEvent(LoginEvent.ResetMessage)
            }
        }
    }

    BackHandler(enabled = true) {
        if (doubleBackToExitPressedOnce) {
            (context as? Activity)?.finish()
        } else {
            doubleBackToExitPressedOnce = true
            Toast.makeText(context, (R.string.press_again_to_exit), Toast.LENGTH_SHORT).show()

            scope.launch {
                delay(2000)
                doubleBackToExitPressedOnce = false
            }
        }
    }

    LaunchedEffect(key1 = state.successLogin) {
        if (state.successLogin) {
            navController.navigate(AppScreens.ChatScreen.route)
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
        if (state.isLogin) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
            }
        } else {
            content(
                state = state,
                onEvent = onEvent,
                context = context,
                navController = navController,
                modifier = Modifier
                    .padding(it)
                    .hideKeyboard(keyboardController = controller, focusManager = focusManager),
            )
        }
    }
}

@Composable
private fun content(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    context: Context,
    navController: NavHostController,
    modifier: Modifier
) {
    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    VassQuickTheme {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (image, title, password, login, biometric, check, columBottom) = createRefs()

                Image(painter = painterResource(id = R.drawable.icon),
                    contentDescription = "",
                    modifier = Modifier
                        .size(120.dp)
                        .constrainAs(image) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                Text(
                    text = stringResource(R.string.login_login),
                    fontSize = 30.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .constrainAs(title) {
                            top.linkTo(image.bottom, margin = 50.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                )

                LoginTextField(state, onEvent, modifier = Modifier.constrainAs(login) {
                    top.linkTo(title.bottom, margin = 20.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

                PasswordTextfield(
                    state = state,
                    onEvent = onEvent,
                    keyboardController = keyboardController,
                    focusManager = focusManager,
                    modifier = Modifier.constrainAs(password) {
                        top.linkTo(login.bottom, margin = 5.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                RememberLogin(
                    state = state,
                    onEvent = onEvent,
                    modifier = Modifier.constrainAs(check) {
                        top.linkTo(password.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )

                val biometricVisible =
                    Biometric(context, onEvent, modifier = Modifier.constrainAs(biometric) {
                        top.linkTo(check.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })

                Column(horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(start = 15.dp, end = 15.dp, bottom = 20.dp)
                        .constrainAs(columBottom) {
                            top.linkTo(
                                if (biometricVisible) biometric.bottom else check.bottom,
                                margin = 20.dp
                            )
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }) {

                    LoginButton(state = state, context = context, onEvent = onEvent)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.don_t_have_an_account),
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                        ClickableText(text = AnnotatedString(stringResource(R.string.sign_up_login)),
                            style = TextStyle(color = Blue40, fontSize = 15.sp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            onClick = {
                                navController.navigate(AppScreens.RegisterScreen.route)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Biometric(
    context: Context,
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier
): Boolean {
    val biometricVisible = hasBiometricCapability(context) && SecureStorage.contain(
        context, TOKEN_KEY
    ) && Storage.contain(
        context, HAVE_BIOMETRIC
    )
    if (biometricVisible) {
        OutlinedButton(
            onClick = {
                biometricLogin(context = context, onEvent = onEvent)
            }, modifier = modifier
                .size(100.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Fingerprint,
                contentDescription = "search",
                tint = Blue40,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
    return biometricVisible
}

@Composable
private fun RememberLogin(
    state: LoginState,
    modifier: Modifier = Modifier,
    onEvent: (LoginEvent) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp)
    ) {
        Checkbox(checked = state.checkSaveUser, onCheckedChange = {
            onEvent(LoginEvent.ReminderLogin)
        })
        Text(
            text = stringResource(R.string.remember_login),
            color = MaterialTheme.colorScheme.onSecondary,
            fontSize = 15.sp,
            modifier = Modifier.padding(end = 5.dp)
        )
    }
}

@Composable
private fun LoginButton(
    state: LoginState,
    context: Context,
    onEvent: (LoginEvent) -> Unit
) {
    Button(
        onClick = {
            val biometricOn = if (Storage.contain(context = context, key = HAVE_BIOMETRIC)) {
                Storage.get(context = context, key = HAVE_BIOMETRIC) == TRUE
            } else {
                false
            }

            if (biometricOn && !SecureStorage.contain(context = context, key = TOKEN_KEY)) {
                loginAndSaveToken(context = context, onEvent = onEvent)
            } else {
                onEvent(LoginEvent.ClickLogin)
            }
        },
        shape = RoundedCornerShape(20),
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = stringResource(R.string.login_uppercase), fontSize = 17.sp)
    }
}

@Composable
private fun PasswordTextfield(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    var passwordVisible by remember { mutableStateOf(false) }

    TextField(
        value = state.password,
        onValueChange = { newValue ->
            onEvent(LoginEvent.ChangePassword(newPassword = newValue))
        },
        label = { Text(stringResource(R.string.password_login)) },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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
                contentDescription = "search",
                tint = if (state.isValidPassword) Red60 else Blue40
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
        supportingText = {
            Text(text = state.errorMessagePassword, color = Red60)

        },
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
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp, top = 5.dp)
    )
}

@Composable
private fun LoginTextField(
    state: LoginState,
    onEvent: (LoginEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = state.login,
        onValueChange = { newValue ->
            onEvent(LoginEvent.ChangeLogin(newLogin = newValue))
        },
        label = { Text(stringResource(R.string.email)) },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.AlternateEmail,
                contentDescription = "search",
                tint = if (state.isValidLogin) Red60 else Blue40
            )
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        shape = RoundedCornerShape(20),
        supportingText = {
            Text(text = state.errorMessageLogin, color = Red60)
        },
        isError = state.isValidLogin,
        colors = TextFieldDefaults.colors().copy(
            unfocusedContainerColor = Grey20,
            focusedContainerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorTrailingIconColor = Red60,
            errorIndicatorColor = Color.Transparent

        ),
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 15.dp, end = 15.dp),
    )
}

private fun biometricLogin(
    context: Context, onEvent: (LoginEvent) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)
    showBiometricPrompt(context = context, executor = executor, onSuccess = {
        onEvent(
            LoginEvent.BiometricAuthentication
        )
    },
        onError = {}
    )
}


private fun loginAndSaveToken(
    context: Context, onEvent: (LoginEvent) -> Unit
) {
    val executor = ContextCompat.getMainExecutor(context)
    showBiometricPrompt(context = context, executor = executor, onSuccess = {
        onEvent(
            LoginEvent.LoginAndSaveToken
        )
    }, onError = {})
}