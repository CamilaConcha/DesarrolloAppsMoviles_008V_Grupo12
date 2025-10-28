package com.example.usagi_tienda_app.data

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log

object CouponStore {
    // estado del cupon aplicado con manejo de errores
    private var _appliedCoupon by mutableStateOf<String?>(null)
    val appliedCoupon: String? get() = _appliedCoupon

    // descuento actual con validacion
    private var _discount by mutableStateOf(0.0)
    val discount: Double get() = _discount

    // cupones validos con sus descuentos
    private val validCoupons = mapOf(
        "USAGI10" to 0.10,
        "CHIIKAWA5" to 0.05,
        "WELCOME15" to 0.15,
        "STUDENT20" to 0.20
    )

    // aplicar cupon con validacion completa
    fun apply(couponCode: String?): Boolean {
        return try {
            // validar entrada
            if (couponCode.isNullOrBlank()) {
                Log.w("CouponStore", "intento de aplicar cupon vacio o nulo")
                return false
            }

            // normalizar codigo (mayusculas, sin espacios)
            val normalizedCode = couponCode.trim().uppercase()
            
            // validar longitud del codigo
            if (normalizedCode.length > 20) {
                Log.w("CouponStore", "codigo de cupon demasiado largo: ${normalizedCode.length} caracteres")
                return false
            }

            // validar caracteres permitidos (solo letras y numeros)
            if (!normalizedCode.matches(Regex("^[A-Z0-9]+$"))) {
                Log.w("CouponStore", "codigo de cupon contiene caracteres invalidos: $normalizedCode")
                return false
            }

            // verificar si el cupon existe
            val discountValue = validCoupons[normalizedCode]
            if (discountValue == null) {
                Log.w("CouponStore", "cupon no valido: $normalizedCode")
                return false
            }

            // validar que el descuento sea razonable
            if (discountValue < 0.0 || discountValue > 1.0) {
                Log.e("CouponStore", "descuento invalido para cupon $normalizedCode: $discountValue")
                return false
            }

            // verificar si ya hay un cupon aplicado
            if (_appliedCoupon != null) {
                Log.i("CouponStore", "reemplazando cupon existente ${_appliedCoupon} con $normalizedCode")
            }

            // aplicar el cupon
            _appliedCoupon = normalizedCode
            _discount = discountValue

            Log.i("CouponStore", "cupon aplicado exitosamente: $normalizedCode (${(discountValue * 100).toInt()}% descuento)")
            true

        } catch (e: Exception) {
            Log.e("CouponStore", "error aplicando cupon: $couponCode", e)
            false
        }
    }

    // remover cupon aplicado con manejo de errores
    fun remove(): Boolean {
        return try {
            if (_appliedCoupon == null) {
                Log.w("CouponStore", "intento de remover cupon cuando no hay ninguno aplicado")
                return false
            }

            val removedCoupon = _appliedCoupon
            _appliedCoupon = null
            _discount = 0.0

            Log.i("CouponStore", "cupon removido exitosamente: $removedCoupon")
            true

        } catch (e: Exception) {
            Log.e("CouponStore", "error removiendo cupon", e)
            false
        }
    }

    // calcular descuento sobre un monto con validacion
    fun calculateDiscount(amount: Double): Double {
        return try {
            // validar monto de entrada
            if (amount < 0.0) {
                Log.w("CouponStore", "monto negativo para calcular descuento: $amount")
                return 0.0
            }

            if (amount == 0.0) {
                return 0.0
            }

            // validar que el monto no sea excesivamente grande
            if (amount > 1_000_000.0) {
                Log.w("CouponStore", "monto excesivamente grande: $amount")
                return 0.0
            }

            // calcular descuento
            val discountAmount = amount * _discount

            // validar resultado
            if (discountAmount < 0.0 || discountAmount > amount) {
                Log.e("CouponStore", "descuento calculado invalido: $discountAmount para monto $amount")
                return 0.0
            }

            Log.d("CouponStore", "descuento calculado: $discountAmount para monto $amount (${(_discount * 100).toInt()}%)")
            discountAmount

        } catch (e: Exception) {
            Log.e("CouponStore", "error calculando descuento para monto: $amount", e)
            0.0
        }
    }

    // obtener monto final con descuento aplicado
    fun getFinalAmount(originalAmount: Double): Double {
        return try {
            // validar monto original
            if (originalAmount < 0.0) {
                Log.w("CouponStore", "monto original negativo: $originalAmount")
                return originalAmount
            }

            val discountAmount = calculateDiscount(originalAmount)
            val finalAmount = originalAmount - discountAmount

            // validar resultado final
            if (finalAmount < 0.0) {
                Log.e("CouponStore", "monto final negativo: $finalAmount")
                return 0.0
            }

            Log.d("CouponStore", "monto final calculado: $finalAmount (original: $originalAmount, descuento: $discountAmount)")
            finalAmount

        } catch (e: Exception) {
            Log.e("CouponStore", "error calculando monto final para: $originalAmount", e)
            originalAmount
        }
    }

    // verificar si un cupon es valido sin aplicarlo
    fun isValidCoupon(couponCode: String?): Boolean {
        return try {
            if (couponCode.isNullOrBlank()) {
                return false
            }

            val normalizedCode = couponCode.trim().uppercase()
            
            // validar formato
            if (normalizedCode.length > 20 || !normalizedCode.matches(Regex("^[A-Z0-9]+$"))) {
                return false
            }

            validCoupons.containsKey(normalizedCode)

        } catch (e: Exception) {
            Log.e("CouponStore", "error validando cupon: $couponCode", e)
            false
        }
    }

    // obtener informacion del cupon sin aplicarlo
    fun getCouponInfo(couponCode: String?): Pair<Boolean, Double> {
        return try {
            if (couponCode.isNullOrBlank()) {
                return Pair(false, 0.0)
            }

            val normalizedCode = couponCode.trim().uppercase()
            val discountValue = validCoupons[normalizedCode]

            if (discountValue != null) {
                Pair(true, discountValue)
            } else {
                Pair(false, 0.0)
            }

        } catch (e: Exception) {
            Log.e("CouponStore", "error obteniendo info de cupon: $couponCode", e)
            Pair(false, 0.0)
        }
    }

    // limpiar estado (para testing o reset)
    fun clear(): Boolean {
        return try {
            _appliedCoupon = null
            _discount = 0.0
            Log.i("CouponStore", "estado de cupones limpiado")
            true

        } catch (e: Exception) {
            Log.e("CouponStore", "error limpiando estado de cupones", e)
            false
        }
    }

    // obtener lista de cupones disponibles (para UI)
    fun getAvailableCoupons(): List<Pair<String, Double>> {
        return try {
            validCoupons.toList()
        } catch (e: Exception) {
            Log.e("CouponStore", "error obteniendo cupones disponibles", e)
            emptyList()
        }
    }
}