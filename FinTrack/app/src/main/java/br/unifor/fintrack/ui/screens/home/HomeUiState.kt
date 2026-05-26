package br.unifor.fintrack.ui.screens.home

import br.unifor.fintrack.domain.model.Transaction

sealed interface HomeUiState {
    data object Loading: HomeUiState
    data class Success (
        val transactions: List<Transaction>,
        val totalBalance: Long,
        val percentageChange: Double
    ): HomeUiState {
        val isEmpty: Boolean
            get() = transactions.isEmpty()
    }
    data class Error(val message: String): HomeUiState
}