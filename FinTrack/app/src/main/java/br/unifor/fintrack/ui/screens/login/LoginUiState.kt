package br.unifor.fintrack.ui.screens.login

data class LoginUiState (
    val email: String = "",
    val password:String = "",
    val emailError: String? = null,
    val passwordError: String? = null,
    val generalError: String? = null,
    val isLoading: Boolean = false
) {
    val isLoginButtonEnabled: Boolean
        get() = !isLoading
}