package br.unifor.fintrack.ui.screens.welcome

import kotlinx.serialization.Serializable

sealed interface WelcomeUiState {

    @Serializable
    data object Loading: WelcomeUiState

    @Serializable
    data object NavigateToLogin: WelcomeUiState

    @Serializable
    data object NavigateToHome: WelcomeUiState
}