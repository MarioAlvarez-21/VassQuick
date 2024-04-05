package com.vassteam2.vassquick.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.constants.HAVE_BIOMETRIC
import com.vassteam2.vassquick.domain.constants.LOGIN
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import com.vassteam2.vassquick.domain.util.SecureStorage
import com.vassteam2.vassquick.domain.util.Storage
import com.vassteam2.vassquick.domain.util.decryptResponse
import com.vassteam2.vassquick.domain.util.encryptResponse
import com.vassteam2.vassquick.ui.theme.Red40
import com.vassteam2.vassquick.ui.theme.Red90
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    val useCases: VassQuickUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    init {
        if(Storage.contain(context = context, key = LOGIN)){
            _state.update {
                it.copy(
                    login = Storage.get(context = context, key = LOGIN)!!,
                    checkSaveUser = true
                )
            }
        }
    }

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.ClickLogin -> {
                if (validationLoginAndPassword()) {
                    login()
                }
            }

            is LoginEvent.BiometricAuthentication -> {
                biometricLogin()
            }

            is LoginEvent.ChangeLogin -> {
                _state.update {
                    it.copy(
                        login = event.newLogin
                    )
                }
            }

            is LoginEvent.ChangePassword -> {
                _state.update {
                    it.copy(
                        password = event.newPassword
                    )
                }
            }

            is LoginEvent.ResetMessage -> {
                _state.update {
                    it.copy(
                        personalizedSnackbar = state.value.personalizedSnackbar.copy(message = null)
                    )
                }
            }

            is LoginEvent.ReminderLogin -> {
                _state.update {
                    it.copy(
                        checkSaveUser = !it.checkSaveUser
                    )
                }
            }

            is LoginEvent.LoginAndSaveToken -> {
                if (validationLoginAndPassword()) {
                    loginAndSaveToken()
                }
            }
        }
    }

    private fun biometricLogin() {
        viewModelScope.launch {
            async(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        isLogin = true
                    )
                }
            }

            val token = withContext(Dispatchers.Default) {
                decryptResponse(key = TOKEN_KEY, context = context)
            }
            val response = withContext(Dispatchers.IO) {
                useCases.biometricUseCase(authtoken = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    async(Dispatchers.Default) {
                        encryptResponse(
                            key = TOKEN_KEY,
                            value = response.data.token,
                            context = context
                        )
                    }
                    UserSingleton.user = response.data.user
                    UserSingleton.token = response.data.token
                    _state.update {
                        it.copy(
                            successLogin = true,
                            errorMessageLogin = "",
                            errorMessagePassword = "",
                        )
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40,
                            ),
                            isLogin = false
                        )
                    }
                }
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            async(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        isLogin = true
                    )
                }
            }
            val response = withContext(Dispatchers.IO) {
                useCases.loginUseCase(
                    login = state.value.login,
                    password = state.value.password
                )
            }
            when (response) {
                is ApiResponse.Success -> {
                    UserSingleton.user = response.data.user
                    UserSingleton.token = response.data.token
                    _state.update {
                        it.copy(
                            successLogin = true,
                            errorMessagePassword = "",
                            errorMessageLogin = ""
                        )
                    }

                    async(Dispatchers.Main) {
                        SecureStorage.delete(context = context, key = TOKEN_KEY)
                        Storage.delete(context = context, key = HAVE_BIOMETRIC)
                        if(state.value.checkSaveUser){
                            Storage.save(context = context, key = LOGIN, value = state.value.login)
                        }
                        else {
                            Storage.delete(context = context, key = LOGIN)
                        }
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40
                            ),
                            isLogin = false
                        )
                    }
                }
            }
        }
    }

    private fun loginAndSaveToken() {
        viewModelScope.launch {
            async(Dispatchers.Main) {
                _state.update {
                    it.copy(
                        isLogin = true
                    )
                }
            }
            val response = withContext(Dispatchers.IO) {
                useCases.loginUseCase(
                    login = state.value.login,
                    password = state.value.password
                )
            }

            when (response) {
                is ApiResponse.Success -> {
                    UserSingleton.user = response.data.user
                    UserSingleton.token = response.data.token
                    async(Dispatchers.Default) {
                        encryptResponse(
                            key = TOKEN_KEY,
                            value = response.data.token,
                            context = context
                        )
                    }
                    _state.update {
                        it.copy(
                            successLogin = true,
                            errorMessagePassword = "",
                            errorMessageLogin = ""
                        )
                    }
                    if(state.value.checkSaveUser){
                        Storage.save(context = context, key = LOGIN, value = state.value.login)
                    }
                    else {
                        Storage.delete(context = context, key = LOGIN)
                    }
                }

                is ApiResponse.Error -> {
                    _state.update {
                        it.copy(
                            personalizedSnackbar = state.value.personalizedSnackbar.copy
                                (
                                message = response.message,
                                containerColor = Red90,
                                contentColor = Red40
                            ),
                            isLogin = false
                        )
                    }
                }
            }
        }
    }


    private fun validationLoginAndPassword(): Boolean {
        return if (_state.value.login.isEmpty() && _state.value.password.isEmpty()) {
            _state.update {
                it.copy(
                    errorMessageLogin = context.getString(R.string.login_cannot_be_empty),
                    isValidLogin = true
                )
            }
            _state.update {
                it.copy(
                    errorMessagePassword = context.getString(R.string.password_cannot_be_empty),
                    isValidPassword = true
                )
            }
            false
        } else if (_state.value.login.isEmpty()) {
            _state.update {
                it.copy(
                    errorMessageLogin = context.getString(R.string.login_cannot_be_empty),
                    isValidLogin = true
                )
            }
            _state.update { it.copy(errorMessagePassword = "", isValidPassword = false) }
            false
        } else if (_state.value.password.isEmpty()) {
            _state.update {
                it.copy(
                    errorMessagePassword = context.getString(R.string.password_cannot_be_empty),
                    isValidPassword = true
                )
            }
            _state.update { it.copy(errorMessageLogin = "", isValidLogin = false) }
            false
        } else {
            _state.update { it.copy(errorMessagePassword = "", isValidPassword = false) }
            _state.update { it.copy(errorMessageLogin = "", isValidLogin = false) }
            true
        }
    }


}