package br.unifor.fintrack.ui.screens.login

sealed interface LoginUiEvent {
    data object NavigateToHome: LoginUiEvent
    data object NavigateToSignUp: LoginUiEvent
}