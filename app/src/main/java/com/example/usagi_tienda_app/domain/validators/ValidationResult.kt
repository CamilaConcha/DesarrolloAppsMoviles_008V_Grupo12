package com.example.usagi_tienda_app.domain.validators

/**
 * Resultado estándar de validación de campo.
 * - `isValid`: indica si pasa la regla.
 * - `error`: mensaje amigable para mostrar en la UI (null si no hay error).
 */
data class ValidationResult(val isValid: Boolean, val error: String? = null)