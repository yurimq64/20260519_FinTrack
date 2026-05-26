package br.unifor.fintrack.ui.screens.signup

sealed interface SignUpUiEvent {
    data object NavigateToHome : SignUpUiEvent
    data object NavigateToLogin : SignUpUiEvent
    data class OpenUrl(val url: String) : SignUpUiEvent
}