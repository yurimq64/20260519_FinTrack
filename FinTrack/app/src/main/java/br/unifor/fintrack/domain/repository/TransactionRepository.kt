package br.unifor.fintrack.domain.repository

import br.unifor.fintrack.domain.model.Transaction
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun observeTransactions(): Flow<List<Transaction>>
    suspend fun addTransaction(transaction: Transaction): String
    suspend fun deleteTransaction(transactionId: String)
    suspend fun updateTransaction(transaction: Transaction)
}