package com.example.usagi_tienda_app.domain.validators

object ConfirmPasswordValidator {
    /**
     * Valida que la confirmación coincida con la contraseña.
     * - Evita errores de tipeo al registrar.
     */
    fun validate(password: String, confirm: String): ValidationResult {
        if (password != confirm) return ValidationResult(false, "Las contraseñas no coinciden")
        return ValidationResult(true)
    }
}