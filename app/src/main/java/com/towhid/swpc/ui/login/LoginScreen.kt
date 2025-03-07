package com.towhid.swpc.ui.login

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.towhid.swpc.R
import com.towhid.swpc.viewmodel.AuthViewModel

@Composable
fun LoginScreen(viewModel: AuthViewModel = viewModel()) {
    val loginResult by viewModel.loginResult.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.login_animation))
    val progress by animateLottieCompositionAsState(
        isPlaying = true,
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.7f
    )
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(loginResult) {
        Log.d("LoginScreen", "Login result: $loginResult")
    }
    LaunchedEffect(loginResult) {
        if (loginResult != null) {
            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
            isLoading = false;
        }
    }

    LaunchedEffect(errorMessage) {
        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            isLoading = false;
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LottieAnimation(
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.CenterHorizontally),
            composition = composition,
            progress = { progress }
        )

        Text("Login", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(emailError.ifEmpty { "Email" }, color = if (emailError.isNotEmpty()) Color.Red else Color.Unspecified) },
            leadingIcon = {
                Icon(Icons.Rounded.AccountCircle, contentDescription = "")
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(passwordError.ifEmpty { "Password" }, color = if (passwordError.isNotEmpty()) Color.Red else Color.Unspecified) },
            leadingIcon = {
                Icon(Icons.Rounded.Lock, contentDescription = "")
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) "Hide password" else "Show password"
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 20.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                emailError = if (email.isBlank()) "Email is required" else ""
                passwordError = if (password.isBlank()) "Password is required" else ""
                if (emailError.isEmpty() && passwordError.isEmpty()) {
                    isLoading = true;
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(90.dp),
            enabled = !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
            } else {
                Text("Login")
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Forget Password?",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable { }
        )
    }
}

@Composable
fun ProtectedDataScreen(viewModel: AuthViewModel = viewModel()) {
    val weatherForecasts by viewModel.weatherForecasts.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = { viewModel.getProtectedData() }) {
            Text("Get Weather Forecast")
        }

        if (weatherForecasts != null) {
            weatherForecasts?.forEach { forecast ->
                Text("Date: ${forecast.date}")
                Text("Temperature C: ${forecast.temperatureC}")
                Text("Temperature F: ${forecast.temperatureF}")
                Text("Summary: ${forecast.summary}")
                Text("---")
            }
        }

        if (errorMessage != null) {
            Text("Error: $errorMessage")
        }

        Button(onClick = { viewModel.logout() }) {
            Text("Logout")
        }
    }
}