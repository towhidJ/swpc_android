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
import androidx.navigation.compose.rememberNavController
import com.towhid.swpc.routes.Routes
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

    var token by remember { mutableStateOf<String?>(null) }
    var userId by remember { mutableStateOf<Int?>(null) }

    // Check authentication state
    LaunchedEffect(Unit) {
        val fetchedToken = TokenManager.getGeneralToken(context)
        val user = TokenManager.getUserId(context)
        token = fetchedToken
        userId=user;
        authState = AuthState.Authenticated
    }

    // Display UI based on authentication state
    when (authState) {
        AuthState.Loading -> {
            CircularProgressIndicator()
        }
        AuthState.Authenticated -> {
            token?.let { userId?.let { it1 -> AuthenticatedNavHost(it, it1,navController) } }
        }
        AuthState.Unauthenticated -> {
            UnauthenticatedNavHost(navController)
        }
    }
}

@Composable
fun AuthenticatedNavHost(token:String,userId:Int,navController: NavHostController) {



    NavHost(navController = navController, startDestination = Routes.MainRoute.DeviceList.route) {
        composable(Routes.MainRoute.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.MainRoute.DeviceList.route) {
            if (token!=null && userId!= null ) {

                 DeviceListScreen(token,userId,navController)
            } else {
                LoginScreen(navController)
                // Handle error: userId is missing or invalid
                Log.e("MainNavigation", "Invalid userId in DeviceList route")
            }
        }
    }
}

@Composable
fun UnauthenticatedNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Routes.MainRoute.Login.route) {
        composable(Routes.MainRoute.Login.route) {
            LoginScreen(navController)
        }
        composable(Routes.MainRoute.Home.route) {
            HomeScreen(navController)
        }

    }
}

sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}

