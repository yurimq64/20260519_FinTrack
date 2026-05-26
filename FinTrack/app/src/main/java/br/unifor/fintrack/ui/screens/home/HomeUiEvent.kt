package br.unifor.fintrack.ui.screens.home

sealed interface HomeUiEvent {
    data object NavigateToReports: HomeUiEvent
    data class NavigateToTransactionDetails(val transactionId: String): HomeUiEvent

    data object NavigateToLogin: HomeUiEvent
}