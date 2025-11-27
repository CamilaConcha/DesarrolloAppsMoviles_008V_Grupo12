package com.example.usagi_tienda_app.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.usagi_tienda_app.Routes
import com.example.usagi_tienda_app.presentation.auth.AuthViewModel
import com.example.usagi_tienda_app.presentation.auth.AuthViewModelFactory
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.usagi_tienda_app.theme.UsagiTheme
import android.content.res.Configuration
import android.util.Log
import com.example.usagi_tienda_app.ui.components.UsagiTopBar

@Composable
fun LoginScreen(navController: NavController) {
    // obtenemos el viewmodel con un factory que necesita el contexto
    // observamos el estado de la pantalla de login compose y lifecycle
    // snackbar para mostrar mensajes errores o exito
    // si hay un mensaje en el estado lo mostramos en el snackbar
    // si el login fue exitoso navegamos a home y limpiamos el back stack
    // campo de correo electronico
    // campo de contrasena
    // boton para enviar el login
    // boton para ir a la pantalla de registro
    
    val vm: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current))
    
    // observamos el estado de la pantalla de login compose y lifecycle
    val state by vm.loginUiState.collectAsStateWithLifecycle()
    // snackbar para mostrar mensajes errores o exito
    val snackbarHostState = remember { SnackbarHostState() }

    // si hay un mensaje en el estado lo mostramos en el snackbar
    LaunchedEffect(state.message) {
        try {
            state.message?.let { 
                if (it.isNotBlank()) {
                    snackbarHostState.showSnackbar(it) 
                }
            }
        } catch (e: Exception) {
            Log.e("LoginScreen", "error mostrando snackbar", e)
        }
    }

    // si el login fue exitoso navegamos a home y limpiamos el back stack
    LaunchedEffect(state.loginSuccess) {
        try {
            if (state.loginSuccess) {
                navController.navigate(Routes.HOME) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                }
            }
        } catch (e: Exception) {
            Log.e("LoginScreen", "error navegando despues del login", e)
        }
    }

    Scaffold(
        topBar = {
            UsagiTopBar(
                navController = navController,
                title = "Usagi Tienda",
                showBack = false
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Iniciar sesión", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            // campo de correo electronico
            OutlinedTextField(
                value = state.email,
                onValueChange = { value ->
                    try {
                        if (value.length <= 100) { // limite de caracteres
                            vm.onLoginEmailChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "error cambiando email", e)
                    }
                },
                label = { Text("Correo electrónico (@duoc.cl)") },
                isError = state.errorEmail != null,
                supportingText = { 
                    if (state.errorEmail != null) {
                        Text(state.errorEmail!!)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(8.dp))

            // campo de contrasena
            OutlinedTextField(
                value = state.password,
                onValueChange = { value ->
                    try {
                        if (value.length <= 50) { // limite de caracteres
                            vm.onLoginPasswordChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "error cambiando password", e)
                    }
                },
                label = { Text("Contraseña") },
                isError = state.errorPassword != null,
                supportingText = { 
                    if (state.errorPassword != null) {
                        Text(state.errorPassword!!)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(Modifier.height(16.dp))

            // boton para enviar el login
            Button(
                onClick = { 
                    try {
                        if (!state.isLoading) {
                            vm.submitLogin()
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "error enviando login", e)
                    }
                },
                enabled = !state.isLoading && state.email.isNotBlank() && state.password.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Ingresando..." else "Ingresar")
            }

            Spacer(Modifier.height(16.dp))

            // boton para ir a la pantalla de registro
            OutlinedButton(
                onClick = { 
                    try {
                        if (!state.isLoading) {
                            navController.navigate(Routes.REGISTER)
                        }
                    } catch (e: Exception) {
                        Log.e("LoginScreen", "error navegando a registro", e)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                Text("Crear cuenta")
            }
        }
    }
}

@Preview(name = "Login - Light", showBackground = true)
@Composable
fun LoginScreenPreviewLight() {
    UsagiTheme(darkTheme = false) {
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }
}

@Preview(name = "Login - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LoginScreenPreviewDark() {
    UsagiTheme(darkTheme = true) {
        val navController = rememberNavController()
        LoginScreen(navController = navController)
    }
}
