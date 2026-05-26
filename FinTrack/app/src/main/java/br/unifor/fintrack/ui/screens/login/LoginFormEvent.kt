package br.unifor.fintrack.ui.screens.login

sealed interface LoginFormEvent {
    data class EmailChanged(val email: String): LoginFormEvent
    data class PasswordChanged(val password: String): LoginFormEvent
    data object LoginClicked: LoginFormEvent
    data object CreateAccountClicked: LoginFormEvent
}