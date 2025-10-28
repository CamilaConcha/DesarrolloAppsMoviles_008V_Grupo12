package com.example.usagi_tienda_app.domain.usecase

import com.example.usagi_tienda_app.data.FavoriteCategory
import com.example.usagi_tienda_app.data.UserRepository
import com.example.usagi_tienda_app.domain.validators.*

data class FieldErrors(
    val fullName: String? = null,
    val email: String? = null,
    val password: String? = null,
    val confirmPassword: String? = null,
    val phone: String? = null,
    val category: String? = null
)

sealed class RegisterOutcome {
    data class Success(val userId: Long) : RegisterOutcome()
    data class ValidationErrors(val errors: FieldErrors) : RegisterOutcome()
    object EmailAlreadyExists : RegisterOutcome()
    data class Failure(val message: String) : RegisterOutcome()
}

class RegisterUser(private val repository: UserRepository) {
    suspend fun execute(
        fullName: String,
        email: String,
        password: String,
        confirmPassword: String,
        phone: String?,
        favoriteCategory: FavoriteCategory?
    ): RegisterOutcome {
        // Validaciones por campo
        val nameRes = NameValidator.validate(fullName)
        val emailRes = EmailValidator.validate(email)
        val passwordRes = PasswordValidator.validate(password)
        val confirmRes = ConfirmPasswordValidator.validate(password, confirmPassword)
        val phoneRes = PhoneValidator.validate(phone)
        val categoryError = if (favoriteCategory == null) "Selecciona una categoría" else null

        val errors = FieldErrors(
            fullName = nameRes.error,
            email = emailRes.error,
            password = passwordRes.error,
            confirmPassword = confirmRes.error,
            phone = phoneRes.error,
            category = categoryError
        )

        if (!nameRes.isValid || !emailRes.isValid || !passwordRes.isValid || !confirmRes.isValid || !phoneRes.isValid || categoryError != null) {
            return RegisterOutcome.ValidationErrors(errors)
        }

        // Unicidad de email
        if (repository.isEmailTaken(email.trim())) {
            return RegisterOutcome.EmailAlreadyExists
        }

        return when (val res = repository.register(fullName.trim(), email.trim(), password, phone?.trim(), favoriteCategory!!)) {
            is com.example.usagi_tienda_app.data.RegisterResult.Success -> RegisterOutcome.Success(res.userId)
            is com.example.usagi_tienda_app.data.RegisterResult.EmailAlreadyExists -> RegisterOutcome.EmailAlreadyExists
            is com.example.usagi_tienda_app.data.RegisterResult.UnknownError -> RegisterOutcome.Failure(res.message)
        }
    }
}

/**
 * `FieldErrors` agrupa los posibles errores por campo para mostrarlos en la UI.
 */