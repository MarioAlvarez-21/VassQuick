package com.vassteam2.vassquick.presentation.register

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.domain.constants.USER_EXIST
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import com.vassteam2.vassquick.ui.theme.Black
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Blue80
import com.vassteam2.vassquick.ui.theme.Grey20
import com.vassteam2.vassquick.ui.theme.Red40
import com.vassteam2.vassquick.ui.theme.Red90
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    val useCases: VassQuickUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.ClickRegister -> {
                if (validationNick() && validationLogin()
                    && validationPassword() && validationRepeatPassword()
                ) {
                    register()
                }
            }

            is RegisterEvent.ChangeLogin -> {
                _state.update {
                    it.copy(
                        login = event.newLogin
                    )
                }
            }

            is RegisterEvent.ChangePassword -> {
                _state.update {
                    it.copy(
                        password = event.newPassword
                    )
                }
            }

            is RegisterEvent.ChangeNick -> {
                _state.update {
                    it.copy(
                        nick = event.newNick
                    )
                }
            }

            is RegisterEvent.ChangeRepeatPassword -> {
                _state.update {
                    it.copy(
                        repeatPassword = event.newRepeatPassword
                    )
                }
            }

            is RegisterEvent.ResetMessage -> {
                _state.update {
                    it.copy(
                        personalizedSnackbar = state.value.personalizedSnackbar.copy(message = null)
                    )
                }
            }
        }
    }

    private fun register() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = withContext(Dispatchers.IO) {
                useCases.registerUseCase(
                    login = state.value.login,
                    password = state.value.password,
                    nick = state.value.nick
                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    _state.update {
                        it.copy(
                            successRegister = true,
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = context.getString(R.string.user_successfully_registered),
                                containerColor = Blue80,
                                contentColor = Blue20
                            ),
                            nick = "",
                            login = "",
                            password = "",
                            repeatPassword = "",
                            messageNick = "",
                            messageLogin = "",
                            messagePassword = "",
                            messageRepeatPassword = "",
                        )
                    }
                }

                is ApiResponse.Error -> {
                    val responseMessage = if (response.message == USER_EXIST) {
                        context.getString(R.string.user_exist)
                    } else {
                        context.getString(R.string.user_registration_error)
                    }
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = responseMessage,
                                containerColor = Red90,
                                contentColor = Red40
                            )
                        )
                    }
                }
            }
        }
    }

    private fun validationNick(): Boolean {
        return if (_state.value.nick.isEmpty()) {
            _state.update {
                it.copy(
                    messageNick = context.getString(R.string.validation_nick1),
                    isValidNick = true
                )
            }
            false
        } else if (_state.value.nick.length < 3) {
            _state.update {
                it.copy(
                    messageNick = context.getString(R.string.validation_nick2),
                    isValidNick = true
                )
            }
            false
        } else {
            _state.update { it.copy(messageNick = "", isValidNick = false) }
            true
        }
    }

    private fun validationPassword(): Boolean {
        return if (_state.value.password.isEmpty()) {
            _state.update {
                it.copy(
                    messagePassword = context.getString(R.string.validation_password1),
                    isValidPassword = true
                )
            }
            false
        } else if (_state.value.password.length < 6) {
            _state.update {
                it.copy(
                    messagePassword = context.getString(R.string.validation_password2),
                    isValidPassword = true
                )
            }
            false
        } else {
            _state.update { it.copy(messagePassword = "", isValidPassword = false) }
            true
        }

    }

    private fun validationRepeatPassword(): Boolean {
        return if (_state.value.repeatPassword.isEmpty()) {
            _state.update {
                it.copy(
                    messageRepeatPassword = context.getString(R.string.validation_password1),
                    isValidRepeatPassword = true
                )
            }
            false
        } else if (_state.value.password != state.value.repeatPassword) {
            _state.update {
                it.copy(
                    messageRepeatPassword = context.getString(R.string.validation_password3),
                    isValidRepeatPassword = true
                )
            }
            false
        } else {
            _state.update { it.copy(messageRepeatPassword = "", isValidRepeatPassword = false) }
            true
        }

    }

    private fun validationLogin(): Boolean {
        return if (_state.value.login.isEmpty()) {
            _state.update {
                it.copy(
                    messageLogin = context.getString(R.string.validation_login1),
                    isValidLogin = true
                )
            }
            false
        } else if (_state.value.login.length < 3) {
            _state.update {
                it.copy(
                    messageLogin = context.getString(R.string.validation_login2),
                    isValidLogin = true
                )
            }
            false
        } else {
            _state.update { it.copy(messageLogin = "", isValidLogin = false) }
            true
        }

    }

}