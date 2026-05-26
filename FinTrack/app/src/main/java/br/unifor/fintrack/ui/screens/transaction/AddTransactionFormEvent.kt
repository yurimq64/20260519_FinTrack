package br.unifor.fintrack.ui.screens.transaction

import br.unifor.fintrack.domain.model.TransactionCategory
import br.unifor.fintrack.domain.model.TransactionType
import java.time.LocalDate

interface AddTransactionFormEvent {
    data class TypeChanged(val type: TransactionType) : AddTransactionFormEvent
    data class AmountDigitPressed(val digit: Int) : AddTransactionFormEvent
    data object AmountBackspacePressed : AddTransactionFormEvent
    data class CategoryChanged(val category: TransactionCategory) : AddTransactionFormEvent
    data class DateChanged(val date: LocalDate) : AddTransactionFormEvent
    data class NoteChanged(val note: String) : AddTransactionFormEvent
    data object SaveClicked : AddTransactionFormEvent
    data object BackClicked : AddTransactionFormEvent
}