package com.example.usagi_tienda_app.domain.validators

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe

class ValidatorsTest : BehaviorSpec({

    Given("EmailValidator duoc.cl") {
        When("email válido @duoc.cl") {
            Then("retorna isValid=true") {
                EmailValidator.validate("alumno@duoc.cl").isValid.shouldBeTrue()
            }
        }
        When("email inválido dominio distinto") {
            Then("retorna isValid=false") {
                val result = EmailValidator.validate("alumno@gmail.com")
                result.isValid.shouldBeFalse()
                result.error shouldBe "Debe ser un email @duoc.cl válido"
            }
        }
    }

    Given("PasswordValidator requisitos mínimos") {
        When("password cumple todos los requisitos") {
            Then("retorna isValid=true") {
                // mínimo 10 caracteres, mayúscula, minúscula, número y especial
                PasswordValidator.validate("Abcdefgh1?").isValid.shouldBeTrue()
            }
        }
        When("password sin mayúscula") {
            Then("retorna isValid=false") {
                PasswordValidator.validate("abcdef1?").isValid.shouldBeFalse()
            }
        }
    }

    Given("ConfirmPasswordValidator coincidencia") {
        When("confirmación igual a password") {
            Then("retorna isValid=true") {
                ConfirmPasswordValidator.validate("Usagi123?","Usagi123?").isValid.shouldBeTrue()
            }
        }
        When("confirmación distinta a password") {
            Then("retorna isValid=false") {
                ConfirmPasswordValidator.validate("Usagi123?","Usagi12?").isValid.shouldBeFalse()
            }
        }
    }
})