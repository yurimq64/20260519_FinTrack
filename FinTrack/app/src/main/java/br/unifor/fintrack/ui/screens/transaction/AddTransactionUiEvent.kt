package br.unifor.fintrack.ui.screens.transaction

sealed interface AddTransactionUiEvent {
    data object NavigateBack: AddTransactionUiEvent
    data object TransactionSaved: AddTransactionUiEvent
}