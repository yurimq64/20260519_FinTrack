package br.unifor.fintrack.ui.screens.transaction

import br.unifor.fintrack.domain.model.TransactionCategory
import br.unifor.fintrack.domain.model.TransactionType
import java.time.LocalDate

data class AddTransactionUiState(
    val type: TransactionType = TransactionType.EXPENSE,
    val amountInCents: Long = 0L,
    val category: TransactionCategory? = null,
    val date: LocalDate = LocalDate.now(),
    val note: String = "",
    val amountError: String? = null,
    val categoryError: String? = null,
    val isSaving: Boolean = false,
    val generalError: String? = null
) {

    val isSaveEnabled: Boolean
        get() = !isSaving

    val availableCategories: List<TransactionCategory>
        get() = TransactionCategory.forType(type)
}
