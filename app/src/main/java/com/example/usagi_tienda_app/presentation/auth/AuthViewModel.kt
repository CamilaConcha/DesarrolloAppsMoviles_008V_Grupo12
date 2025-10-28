package com.example.usagi_tienda_app.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.usagi_tienda_app.data.FavoriteCategory
import com.example.usagi_tienda_app.data.LoginResult
import com.example.usagi_tienda_app.data.UserRepository
import com.example.usagi_tienda_app.domain.usecase.LoginUser
import com.example.usagi_tienda_app.domain.usecase.RegisterOutcome
import com.example.usagi_tienda_app.domain.usecase.RegisterUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel de autenticación.
 * - Mantiene y expone el estado de Login y Registro.
 * - Provee handlers para cambios en inputs y para enviar las acciones.
 */
class AuthViewModel(private val repository: UserRepository) : ViewModel() {
    private val registerUser = RegisterUser(repository)
    private val loginUser = LoginUser(repository)

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState: StateFlow<LoginUiState> = _loginUiState

    private val _registerUiState = MutableStateFlow(RegisterUiState())
    val registerUiState: StateFlow<RegisterUiState> = _registerUiState

    fun onLoginEmailChange(value: String) {
        _loginUiState.value = _loginUiState.value.copy(email = value, errorEmail = null, message = null)
    }

    fun onLoginPasswordChange(value: String) {
        _loginUiState.value = _loginUiState.value.copy(password = value, errorPassword = null, message = null)
    }

    fun submitLogin() {
        val state = _loginUiState.value
        _loginUiState.value = state.copy(isLoading = true, message = null)
        viewModelScope.launch {
            when (val res = loginUser.execute(state.email, state.password)) {
                LoginResult.Success -> {
                    _loginUiState.value = _loginUiState.value.copy(isLoading = false, loginSuccess = true, message = "Bienvenido")
                }
                LoginResult.EmailNotFound -> {
                    _loginUiState.value = _loginUiState.value.copy(isLoading = false, errorEmail = "Email no registrado")
                }
                LoginResult.IncorrectPassword -> {
                    _loginUiState.value = _loginUiState.value.copy(isLoading = false, errorPassword = "Contraseña incorrecta")
                }
            }
        }
    }

    fun onRegisterFullNameChange(v: String) { _registerUiState.value = _registerUiState.value.copy(fullName = v, errorFullName = null) }
    fun onRegisterEmailChange(v: String) { _registerUiState.value = _registerUiState.value.copy(email = v, errorEmail = null) }
    fun onRegisterPasswordChange(v: String) { _registerUiState.value = _registerUiState.value.copy(password = v, errorPassword = null) }
    fun onRegisterConfirmPasswordChange(v: String) { _registerUiState.value = _registerUiState.value.copy(confirmPassword = v, errorConfirmPassword = null) }
    fun onRegisterPhoneChange(v: String?) { _registerUiState.value = _registerUiState.value.copy(phone = v, errorPhone = null) }
    fun onRegisterCategoryChange(c: FavoriteCategory?) { _registerUiState.value = _registerUiState.value.copy(favoriteCategory = c, errorCategory = null) }

    fun submitRegister() {
        val s = _registerUiState.value
        _registerUiState.value = s.copy(isLoading = true, successUserId = null, message = null)
        viewModelScope.launch {
            when (val out = registerUser.execute(
                fullName = s.fullName,
                email = s.email,
                password = s.password,
                confirmPassword = s.confirmPassword,
                phone = s.phone,
                favoriteCategory = s.favoriteCategory
            )) {
                is RegisterOutcome.Success -> {
                    _registerUiState.value = _registerUiState.value.copy(isLoading = false, successUserId = out.userId, message = "Cuenta creada")
                }
                is RegisterOutcome.ValidationErrors -> {
                    _registerUiState.value = _registerUiState.value.copy(
                        isLoading = false,
                        errorFullName = out.errors.fullName,
                        errorEmail = out.errors.email,
                        errorPassword = out.errors.password,
                        errorConfirmPassword = out.errors.confirmPassword,
                        errorPhone = out.errors.phone,
                        errorCategory = out.errors.category
                    )
                }
                RegisterOutcome.EmailAlreadyExists -> {
                    _registerUiState.value = _registerUiState.value.copy(isLoading = false, errorEmail = "El email ya está registrado")
                }
                is RegisterOutcome.Failure -> {
                    _registerUiState.value = _registerUiState.value.copy(isLoading = false, message = out.message)
                }
            }
        }
    }

    fun clearRegisterSuccess() {
        _registerUiState.value = _registerUiState.value.copy(successUserId = null)
    }
}