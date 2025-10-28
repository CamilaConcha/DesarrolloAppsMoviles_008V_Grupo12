package com.example.usagi_tienda_app.domain.validators

object PhoneValidator {
    private val regex = Regex("^[+0-9 ]{8,15}$")

    /**
     * Valida el teléfono móvil (opcional).
     * - Si viene vacío, se considera válido; si trae valor, debe cumplir formato.
     */
    fun validate(phone: String?): ValidationResult {
        val p = phone?.trim()
        if (p.isNullOrEmpty()) return ValidationResult(true)
        if (!regex.matches(p)) return ValidationResult(false, "Teléfono inválido")
        return ValidationResult(true)
    }
}