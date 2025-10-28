package com.example.usagi_tienda_app.domain.validators

object PasswordValidator {
    /**
     * Valida la contraseña.
     * - Debe tener mínimo 6 caracteres.
     */
    fun validate(password: String): ValidationResult {
        if (password.length < 10) return ValidationResult(false, "Mínimo 10 caracteres")
        if (!password.any { it.isUpperCase() }) return ValidationResult(false, "Debe incluir al menos 1 mayúscula")
        if (!password.any { it.isLowerCase() }) return ValidationResult(false, "Debe incluir al menos 1 minúscula")
        if (!password.any { it.isDigit() }) return ValidationResult(false, "Debe incluir al menos 1 número")
        val specialChars = "@#$%&*!?".toSet()
        if (!password.any { it in specialChars }) return ValidationResult(false, "Debe incluir al menos 1 carácter especial (@#$%&*!?)")
        return ValidationResult(true)
    }
}