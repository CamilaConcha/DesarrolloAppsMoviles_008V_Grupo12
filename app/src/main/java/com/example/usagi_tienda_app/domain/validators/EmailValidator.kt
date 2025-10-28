package com.example.usagi_tienda_app.domain.validators

object EmailValidator {
    private val emailRegex = Regex("^[A-Za-z0-9+_.-]+@duoc\\.cl$")

    /**
     * Valida formato básico de correo electrónico.
     * - Retorna `ValidationResult` con `isValid` y `error`.
     */
    fun validate(email: String): ValidationResult {
        val e = email.trim()
        if (e.isEmpty()) return ValidationResult(false, "El email no debe estar vacío")
        if (e.length > 60) return ValidationResult(false, "Máximo 60 caracteres")
        if (!emailRegex.matches(e)) return ValidationResult(false, "Debe ser un email @duoc.cl válido")
        return ValidationResult(true)
    }
}