package br.unifor.fintrack.domain.model

import java.time.LocalDateTime
import java.util.UUID

data class Transaction(
    val id: String = UUID.randomUUID().toString(),
    val description: String,
    val amountInCents: Long,
    val type: TransactionType,
    val category: TransactionCategory,
    val date: LocalDateTime,
    val note: String? = null
) {
    val signedAmountInCents: Long
        get() = when (type) {
            TransactionType.INCOME -> amountInCents
            TransactionType.EXPENSE -> -amountInCents
        }
}
