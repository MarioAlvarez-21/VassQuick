package com.vassteam2.vassquick.navigation

sealed class AppScreens(val route: String){
    object MainScreen : AppScreens("main_screen")
    object VassQuickSplash : AppScreens ("splash_screen")
    object LoginScreen : AppScreens("login_screen")
    object RegisterScreen : AppScreens("register_screen")
    object UsersScreen : AppScreens("users_screen")
    object ChatScreen : AppScreens("chat_screen")
    object ChatRoomScreen : AppScreens("chat_room_screen")
    object ProfileScreen : AppScreens("profile_screen")
    object ProfileEditScreen : AppScreens("profile_edit_screen")
    object ConditionsScreen : AppScreens("conditions_screen")
}
