package com.vassteam2.vassquick.navigation


import AnimatedScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.vassteam2.vassquick.presentation.chat.ChatEvent
import com.vassteam2.vassquick.presentation.chat.ChatScreen
import com.vassteam2.vassquick.presentation.chat.ChatViewModel
import com.vassteam2.vassquick.presentation.chat_room.ChatRoomScreen
import com.vassteam2.vassquick.presentation.chat_room.ChatRoomViewModel
import com.vassteam2.vassquick.presentation.conditions.ConditionsScreen
import com.vassteam2.vassquick.presentation.login.LoginScreen
import com.vassteam2.vassquick.presentation.login.LoginViewModel
import com.vassteam2.vassquick.presentation.main.MainScreen
import com.vassteam2.vassquick.presentation.profile.ProfileScreen
import com.vassteam2.vassquick.presentation.profile.ProfileViewModel
import com.vassteam2.vassquick.presentation.register.RegisterScreen
import com.vassteam2.vassquick.presentation.register.RegisterViewModel
import com.vassteam2.vassquick.presentation.splash.VassQuickSplash
import com.vassteam2.vassquick.presentation.users.UsersScreen
import com.vassteam2.vassquick.presentation.users.UsersViewModel



@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppScreens.VassQuickSplash.route) {
        composable(route = AppScreens.VassQuickSplash.route) {
            AnimatedScreen {
                VassQuickSplash(navController = navController)
            }
        }
        composable(route = AppScreens.MainScreen.route) {
            AnimatedScreen {
                MainScreen(navController = navController)
            }
        }
        composable(route = AppScreens.LoginScreen.route)
        {
            val viewModel = hiltViewModel<LoginViewModel>()
            val state = viewModel.state.collectAsState()
            AnimatedScreen {
                LoginScreen(
                    navController = navController,
                    state = state.value,
                    onEvent = viewModel::onEvent
                )
            }
        }
        composable(route = AppScreens.RegisterScreen.route)
        {
            val viewModel = hiltViewModel<RegisterViewModel>()
            val state = viewModel.state.collectAsState()
            AnimatedScreen {
                RegisterScreen(
                    navController = navController,
                    state = state.value,
                    onEvent = viewModel::onEvent
                )
            }
        }
        composable(route = AppScreens.UsersScreen.route)
        {
            val viewModel = hiltViewModel<UsersViewModel>()
            val state = viewModel.state.collectAsState()
            UsersScreen(
                navController = navController,
                state = state.value,
                onEvent = viewModel::onEvent
            )
        }
        composable(route = AppScreens.ChatScreen.route) {
            val viewModel = hiltViewModel<ChatViewModel>()
            val state = viewModel.state.collectAsState()
            val filteredChats = viewModel.filteredChats.collectAsState()
            LaunchedEffect(key1 = true) {
                viewModel.loadChatsAndLastMessages()
            }
            AnimatedScreen {
                ChatScreen(
                    navController = navController,
                    state = state.value,
                    filteredChats = filteredChats,
                    onEvent = viewModel::onEvent
                )

            }
        }
        composable(
            route = AppScreens.ChatRoomScreen.route + "/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) {
            val viewModel = hiltViewModel<ChatRoomViewModel>()
            val messages = viewModel.messagesFlow?.collectAsLazyPagingItems()
            val state = viewModel.state.collectAsState()
            LaunchedEffect(key1 = true){
                viewModel.start(idChat = it.arguments!!.getString("id")!!)
            }

            ChatRoomScreen(
                messages = messages,
                state = state.value,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable(route = AppScreens.ProfileScreen.route) {
            val viewModel = hiltViewModel<ProfileViewModel>()
            val state = viewModel.state.collectAsState()
            AnimatedScreen {
                ProfileScreen(
                    navController = navController,
                    state = state.value,
                    onEvent = viewModel::onEvent
                )
            }
        }

        composable(route = AppScreens.ConditionsScreen.route) {
            AnimatedScreen {
                ConditionsScreen()
            }
        }
    }
}
