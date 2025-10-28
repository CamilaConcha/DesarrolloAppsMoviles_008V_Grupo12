package com.example.usagi_tienda_app.ui.auth

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.usagi_tienda_app.Routes
import com.example.usagi_tienda_app.data.FavoriteCategory
import com.example.usagi_tienda_app.presentation.auth.AuthViewModel
import com.example.usagi_tienda_app.presentation.auth.AuthViewModelFactory
import com.example.usagi_tienda_app.theme.UsagiTheme
import android.util.Log
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun RegisterScreen(navController: NavController) {
    // obtenemos el viewmodel con un factory que necesita el contexto
    // observamos el estado de la pantalla de registro
    // snackbar para mostrar mensajes
    // mostramos cualquier mensaje que llegue al estado
    // si el registro fue exitoso navegamos al login y limpiamos el back stack
    // campo de correo
    // campo de nombre completo
    // campo de telefono opcional
    // campo de contrasena
    // campo de confirmacion de contrasena
    // seleccion de categoria favorita
    // boton de registro
    // fila de filtros de categoria favorita
    // chip de categoria basico cambia color segun seleccion
    // tarjeta simple muestra nombre precio y categoria
    // implementacion simple de flowrow usando row con espaciado no es real
    
    val vm: AuthViewModel = viewModel(factory = AuthViewModelFactory(LocalContext.current))
    
    // observamos el estado de la pantalla de registro
    val state by vm.registerUiState.collectAsStateWithLifecycle()
    // snackbar para mostrar mensajes
    val snackbarHostState = remember { SnackbarHostState() }

    // mostramos cualquier mensaje que llegue al estado
    LaunchedEffect(state.message) {
        try {
            state.message?.let { 
                if (it.isNotBlank()) {
                    snackbarHostState.showSnackbar(it) 
                }
            }
        } catch (e: Exception) {
            Log.e("RegisterScreen", "error mostrando snackbar", e)
        }
    }

    // si el registro fue exitoso navegamos al login y limpiamos el back stack
    LaunchedEffect(state.successUserId) {
        try {
            if (state.successUserId != null) {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.REGISTER) { inclusive = true }
                }
                vm.clearRegisterSuccess()
            }
        } catch (e: Exception) {
            Log.e("RegisterScreen", "error navegando despues del registro", e)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(text = "Crear cuenta", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(16.dp))

            // campo de correo
            OutlinedTextField(
                value = state.email,
                onValueChange = { value ->
                    try {
                        if (value.length <= 100) { // limite de caracteres
                            vm.onRegisterEmailChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando email", e)
                    }
                },
                label = { Text("Correo duoc.cl") },
                isError = state.errorEmail != null,
                supportingText = { state.errorEmail?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // campo de nombre completo
            OutlinedTextField(
                value = state.fullName,
                onValueChange = { value ->
                    try {
                        if (value.length <= 100) { // limite de caracteres
                            vm.onRegisterFullNameChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando nombre", e)
                    }
                },
                label = { Text("Nombre completo") },
                isError = state.errorFullName != null,
                supportingText = { state.errorFullName?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // campo de telefono opcional
            OutlinedTextField(
                value = state.phone ?: "",
                onValueChange = { value ->
                    try {
                        val cleanValue = value.filter { it.isDigit() || it == '+' }
                        if (cleanValue.length <= 15) { // limite de caracteres para telefono
                            vm.onRegisterPhoneChange(if (cleanValue.isBlank()) null else cleanValue)
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando telefono", e)
                    }
                },
                label = { Text("Teléfono (opcional)") },
                isError = state.errorPhone != null,
                supportingText = { state.errorPhone?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Phone,
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
                            vm.onRegisterPasswordChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando password", e)
                    }
                },
                label = { Text("Contraseña") },
                isError = state.errorPassword != null,
                supportingText = { state.errorPassword?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Next
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(8.dp))

            // campo de confirmacion de contrasena
            OutlinedTextField(
                value = state.confirmPassword,
                onValueChange = { value ->
                    try {
                        if (value.length <= 50) { // limite de caracteres
                            vm.onRegisterConfirmPasswordChange(value)
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando confirmacion password", e)
                    }
                },
                label = { Text("Confirmar contraseña") },
                isError = state.errorConfirmPassword != null,
                supportingText = { state.errorConfirmPassword?.let { Text(it) } },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(Modifier.height(16.dp))

            // seleccion de categoria favorita
            Text(text = "Selecciona tu categoría favorita", style = MaterialTheme.typography.titleMedium)
            if (state.errorCategory != null) {
                Text(
                    text = state.errorCategory!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(Modifier.height(8.dp))
            CategoryFlowRow(
                selected = state.favoriteCategory, 
                onSelected = { category ->
                    try {
                        vm.onRegisterCategoryChange(category)
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error cambiando categoria", e)
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            // boton de registro
            Button(
                onClick = { 
                    try {
                        if (!state.isLoading) {
                            vm.submitRegister()
                        }
                    } catch (e: Exception) {
                        Log.e("RegisterScreen", "error enviando registro", e)
                    }
                },
                enabled = !state.isLoading && 
                         state.email.isNotBlank() && 
                         state.fullName.isNotBlank() && 
                         state.password.isNotBlank() && 
                         state.confirmPassword.isNotBlank() &&
                         state.favoriteCategory != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (state.isLoading) "Creando cuenta..." else "Crear cuenta")
            }
        }
    }
}

@Composable
private fun CategoryFlowRow(selected: FavoriteCategory?, onSelected: (FavoriteCategory?) -> Unit) {
    // fila de filtros de categoria favorita
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FavoriteCategory.values().forEach { cat ->
            CategoryChip(
                category = cat,
                selected = selected == cat,
                onClick = { 
                    try {
                        onSelected(if (selected == cat) null else cat)
                    } catch (e: Exception) {
                        Log.e("CategoryFlowRow", "error seleccionando categoria", e)
                    }
                }
            )
        }
    }
}

@Composable
private fun CategoryChip(category: FavoriteCategory, selected: Boolean, onClick: () -> Unit) {
    // chip de categoria basico cambia color segun seleccion
    val label = when (category) {
        FavoriteCategory.CHIIKAWA -> "Chiikawa"
        FavoriteCategory.PELUCHES -> "Peluches"
        FavoriteCategory.LLAVEROS -> "Llaveros"
        FavoriteCategory.ACCESORIOS -> "Accesorios"
    }
    
    FilterChip(
        onClick = onClick,
        label = { Text(label) },
        selected = selected
    )
}

@Preview(name = "Registro - Light", showBackground = true)
@Composable
fun RegisterScreenPreviewLight() {
    UsagiTheme(darkTheme = false) {
        val navController = rememberNavController()
        RegisterScreen(navController = navController)
    }
}

@Preview(name = "Registro - Dark", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun RegisterScreenPreviewDark() {
    UsagiTheme(darkTheme = true) {
        val navController = rememberNavController()
        RegisterScreen(navController = navController)
    }
}