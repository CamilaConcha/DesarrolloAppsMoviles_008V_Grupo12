package com.example.usagi_tienda_app.domain.validators

object NameValidator {
    private val regex = Regex("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]{1,100}$")

    /**
     * Valida el nombre completo.
     * - Debe tener al menos 3 caracteres y máximo 60.
     */
    fun validate(name: String): ValidationResult {
        val trimmed = name.trim()
        if (trimmed.isEmpty()) return ValidationResult(false, "El nombre no debe estar vacío")
        if (!regex.matches(trimmed)) return ValidationResult(false, "Solo letras y espacios, máximo 100 caracteres")
        return ValidationResult(true)
    }
}