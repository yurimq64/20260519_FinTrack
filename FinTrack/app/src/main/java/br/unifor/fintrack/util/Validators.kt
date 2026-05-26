package br.unifor.fintrack.util

sealed interface ValidationResult {
    data object Valid : ValidationResult
    data class Invalid(val errorMessage: String) : ValidationResult
}

object Validators {

    fun validateEmail(email: String): ValidationResult {
        return when {
            email.isBlank() -> ValidationResult.Invalid("Informe seu endereço de e-mail")
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches() -> ValidationResult.Invalid("Endereço de e-mail inválido")
            else -> ValidationResult.Valid
        }
    }

    fun validatePassword(password:String): ValidationResult {
        return when {
            password.isBlank() -> ValidationResult.Invalid("Informe sua senha")
            password.length < MIN_PASSWORD_LENGTH -> ValidationResult.Invalid("Senha deve ter no mínimo $MIN_PASSWORD_LENGTH")
            else -> ValidationResult.Valid
        }
    }

    fun validateFullName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Invalid("Informe seu nome completo")
            name.trim().length < MIN_NAME_LENGTH ->
                ValidationResult.Invalid("Nome deve ter no mínimo $MIN_NAME_LENGTH caracteres")
            !name.trim().contains(" ") ->
                ValidationResult.Invalid("Informe nome e sobrenome")
            else -> ValidationResult.Valid
        }
    }

    fun validatePasswordConfirmation(
        password: String,
        confirmation: String
    ): ValidationResult {
        return when {
            confirmation.isBlank() -> ValidationResult.Invalid("Confirme sua senha")
            confirmation != password -> ValidationResult.Invalid("As senhas não coincidem")
            else -> ValidationResult.Valid
        }
    }

    private const val MIN_PASSWORD_LENGTH = 6
    private const val MIN_NAME_LENGTH = 3

}