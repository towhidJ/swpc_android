package com.towhid.swpc

import android.util.Log
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.towhid.swpc.routes.Routes
import com.towhid.swpc.routes.Routes.MainRoute.DeviceList.toDeviceList
import com.towhid.swpc.ui.login.LoginScreen
import com.towhid.swpc.ui.view.DeviceListScreen
import com.towhid.swpc.ui.view.HomeScreen
import com.towhid.swpc.util.TokenManager
import kotlinx.coroutines.launch

@Composable
fun MainNavigation() {
    val navController = rememberNavController()
    var authState by remember { mutableStateOf<AuthState>(AuthState.Loading) }

    val context = LocalContext.current
    var startDestination by remember { mutableStateOf<String?>(null) }

    var token by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }

    // Check authentication state
    LaunchedEffect(Unit) {
        val user = TokenManager.getUserId(context)
        token = TokenManager.getGeneralToken(context) ?: ""
        if (user != null && user > 0) {
            startDestination = Routes.MainRoute.DeviceList.toDeviceListString(user)
        } else {
            startDestination = Routes.MainRoute.Login.route
        }
    }

    if(startDestination == null) return
    NavHost(navController = navController, startDestination = startDestination!!) {
        composable(Routes.MainRoute.Login.route) {
            LoginScreen(navController)
        }
        composable(Routes.MainRoute.Home.route) {
            HomeScreen(navController)
        }
        composable(
            route = Routes.MainRoute.DeviceList.route,
            arguments = Routes.MainRoute.DeviceList.arguments
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId")
            if (userId != null) {
                DeviceListScreen(token!!,userId, navController)
            } else {
                // Handle error: userId is missing or invalid
                Log.e("Navigation", "Invalid userId in DeviceList route")
            }
        }
        }
    }



sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}