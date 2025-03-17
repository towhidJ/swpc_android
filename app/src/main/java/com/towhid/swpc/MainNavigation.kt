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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.towhid.swpc.routes.Routes
import com.towhid.swpc.ui.login.LoginScreen
import com.towhid.swpc.ui.view.DeviceListScreen
import com.towhid.swpc.ui.view.HomeScreen
import com.towhid.swpc.util.TokenManager
import kotlinx.coroutines.launch

@Composable
fun MainNavigation(navController: NavHostController) {
    var authState by remember { mutableStateOf<AuthState>(AuthState.Loading) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val token = TokenManager.getGeneralToken(context)
        if (token != null && TokenManager.isTokenValid(context)) {
            authState = AuthState.Authenticated
        } else {
            authState = AuthState.Unauthenticated
        }
    }

    when (authState) {
        AuthState.Loading -> {
            CircularProgressIndicator()
        }
        AuthState.Authenticated -> {
            NavHost(navController = navController, startDestination = Routes.MainRoute.Home.route) {
                composable(Routes.MainRoute.Home.route) {
                    DeviceListScreen(3,navController)
                }
            }
        }
        AuthState.Unauthenticated -> {
            NavHost(navController = navController, startDestination = Routes.MainRoute.Login.route) {
                composable(Routes.MainRoute.Login.route) {
                    LoginScreen(navController)
                }
            }
        }
    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}
