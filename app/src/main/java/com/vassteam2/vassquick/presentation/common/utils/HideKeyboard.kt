package com.vassteam2.vassquick.presentation.common.utils

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.SoftwareKeyboardController

fun dismissKeyboardAndClearFocus(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
) {
    keyboardController?.hide()
    focusManager.clearFocus()
}

fun Modifier.hideKeyboard(
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager
): Modifier {
    return this.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                dismissKeyboardAndClearFocus(
                    keyboardController = keyboardController,
                    focusManager = focusManager
                )
            }
        )
    }
}