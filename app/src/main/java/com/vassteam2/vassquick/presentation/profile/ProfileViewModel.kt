package com.vassteam2.vassquick.presentation.profile

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.data.request.UpdateProfileRequest
import com.vassteam2.vassquick.data.response.ApiResponse
import com.vassteam2.vassquick.di.UserSingleton
import com.vassteam2.vassquick.domain.constants.HAVE_BIOMETRIC
import com.vassteam2.vassquick.domain.constants.TOKEN_KEY
import com.vassteam2.vassquick.domain.constants.TRUE
import com.vassteam2.vassquick.domain.model.PersonalizedSnackbar
import com.vassteam2.vassquick.domain.use_case.VassQuickUseCases
import com.vassteam2.vassquick.domain.util.SecureStorage
import com.vassteam2.vassquick.domain.util.Storage
import com.vassteam2.vassquick.ui.theme.Blue20
import com.vassteam2.vassquick.ui.theme.Blue80
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
class ProfileViewModel @Inject constructor(
    private val useCases: VassQuickUseCases,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val token = UserSingleton.token
            val response = withContext(Dispatchers.IO) {
                useCases.getProfileUseCase(token = token)
            }
            when (response) {
                is ApiResponse.Success -> {
                    val user = response.data
                    val haveBiometric =
                        Storage.get(context = context, key = HAVE_BIOMETRIC) == TRUE
                    _state.update {
                        it.copy(
                            user = user,
                            userCopy = user,
                            checkBiometric = haveBiometric
                        )
                    }
                }

                is ApiResponse.Error -> {
                    response.message
                }
            }
        }
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.ClickSaveUpdate -> {
                updateProfile()
            }

            is ProfileEvent.ChangeNick -> {
                _state.update {
                    it.copy(
                        user = it.user!!.copy(nick = event.newNick)
                    )
                }
            }

            is ProfileEvent.ChangePassword -> {
                _state.update {
                    it.copy(user = it.user!!.copy(password = event.newPassword))
                }
            }

            is ProfileEvent.ResetMessage -> {
                _state.value = _state.value.copy(
                    personalizedSnackbar = PersonalizedSnackbar(
                        message = null,
                        contentColor = Blue80,
                        containerColor = Blue20
                    )
                )
            }

            is ProfileEvent.CheckBiometric -> {
                _state.update {
                    it.copy(
                        checkBiometric = event.checkBiometric
                    )
                }
                if (_state.value.checkBiometric) {
                    Storage.save(context = context, key = HAVE_BIOMETRIC, value = TRUE)
                } else {
                    Storage.delete(context = context, key = HAVE_BIOMETRIC)
                    SecureStorage.delete(context = context, key = TOKEN_KEY)
                }
            }
        }
    }

    private fun updateProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            val token = UserSingleton.token
            val updateRequest = UpdateProfileRequest(
                nick = _state.value.user!!.nick,
                password = _state.value.user!!.password!!
            )

            if (validationNick() && validationPassword()) {
                when (val response = useCases.updateProfileUseCase(token, updateRequest)) {
                    is ApiResponse.Success -> {
                        _state.value = _state.value.copy(
                            successUpdate = true,
                            personalizedSnackbar = PersonalizedSnackbar(
                                message = context.getString(R.string.profile_updated_successfully),
                                contentColor = Blue80,
                                containerColor = Blue20
                            )
                        )
                    }

                    is ApiResponse.Error -> {
                        _state.value = _state.value.copy(
                            successUpdate = false,
                            personalizedSnackbar = PersonalizedSnackbar(
                                message = response.message
                                    ?: context.getString(R.string.error_updating_profile),
                                contentColor = Blue80,
                                containerColor = Blue20
                            )

                        )
                    }
                }
            }
        }
    }

    private fun validationNick(): Boolean {
        return if (_state.value.user!!.nick.isEmpty()) {
            _state.update {
                it.copy(
                    messageNick = context.getString(R.string.validation_nick1),
                    invalideNick = true
                )
            }
            false
        } else if (_state.value.user!!.nick.length < 3) {
            _state.update {
                it.copy(
                    messageNick = context.getString(R.string.validation_nick2),
                    invalideNick = true
                )
            }
            false
        } else {
            _state.update { it.copy(messageNick = "", invalideNick = false) }
            true
        }
    }

    private fun validationPassword(): Boolean {
        return if (_state.value.user!!.password!!.isEmpty()) {
            _state.update {
                it.copy(
                    messagePassword = context.getString(R.string.validation_password1),
                    invalidePassword = true
                )
            }
            false
        } else if (_state.value.user!!.password!!.length < 3) {
            _state.update {
                it.copy(
                    messagePassword = context.getString(R.string.validation_password2),
                    invalidePassword = true
                )
            }
            false
        } else {
            _state.update { it.copy(messagePassword = "", invalidePassword = false) }
            true
        }
    }
}

