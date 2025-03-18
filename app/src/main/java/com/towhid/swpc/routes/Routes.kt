package com.towhid.swpc.routes

import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Routes(val route: String) {

    data object MainRoute : Routes("mainRoutes") {

        data object Login : Routes("${MainRoute.route}/login") {
            fun NavController.toLogin() = navigate("${MainRoute.route}/login")
        }

        data object ForgotPassword : Routes("${MainRoute.route}/forgotPassword") {
            fun NavController.toForgotPassword() = navigate("${MainRoute.route}/forgotPassword")
        }

        data object SignUp : Routes("${MainRoute.route}/signUp") {
            fun NavController.toSignUp() = navigate("${MainRoute.route}/signUp")
        }

        data object Home : Routes("${MainRoute.route}/home") {
            fun NavController.toHome() = navigate("${MainRoute.route}/home")
        }

        object DeviceList :Routes("$route/deviceList/{userId}") {
            fun NavController.toDeviceList(userId: Int) {
                navigate("mainRoutes/deviceList/$userId")
            }

            fun toDeviceListString(userId: Int): String {
                return "mainRoutes/deviceList/$userId"
            }

            val arguments = listOf(
                navArgument("userId") { type = NavType.IntType }
            )
        }
    }

}
