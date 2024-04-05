package com.vassteam2.vassquick.presentation.chat.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.vassteam2.vassquick.R
import com.vassteam2.vassquick.presentation.chat.ChatEvent
import com.vassteam2.vassquick.presentation.common.utils.dismissKeyboardAndClearFocus
import com.vassteam2.vassquick.ui.theme.Blue40
import com.vassteam2.vassquick.ui.theme.Grey20

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchBar(
    onEvent: (ChatEvent) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    focusManager: FocusManager,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    TextField(
        value = searchQuery,
        onValueChange = { newValue ->
            searchQuery = newValue
            onEvent(ChatEvent.SetSearchQuery(newValue))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(onSearch = {
            onEvent(ChatEvent.SetSearchQuery(searchQuery))
            dismissKeyboardAndClearFocus(
                keyboardController = keyboardController,
                focusManager = focusManager
            )
        }),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Seach",
                tint = Blue40,
                modifier = Modifier.clickable {
                    keyboardController?.hide()
                    onEvent(ChatEvent.SetSearchQuery(query = searchQuery))
                }
            )
        },
        label = {
            Text(
                text = stringResource(R.string.search_chat),
            )
        },
        shape = RoundedCornerShape(20),
        colors = TextFieldDefaults.textFieldColors(
            containerColor = Grey20,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        modifier = modifier
    )
}