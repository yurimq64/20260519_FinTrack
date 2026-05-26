package br.unifor.fintrack.ui.screens.signup

data class SignUpUiState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val termsAccepted: Boolean = false,
    val fullNameError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmationError: String? = null,
    val termsError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false
) {
    val isSignUpButtonEnabled: Boolean
        get() = !isLoading
}