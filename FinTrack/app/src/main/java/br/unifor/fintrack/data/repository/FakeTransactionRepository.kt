package br.unifor.fintrack.data.repository

import br.unifor.fintrack.domain.model.Transaction
import br.unifor.fintrack.domain.model.TransactionCategory
import br.unifor.fintrack.domain.model.TransactionType
import br.unifor.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime

object FakeTransactionRepository : TransactionRepository {

    private val _transactions = MutableStateFlow(createInicialTransactions())

    override fun observeTransactions(): Flow<List<Transaction>> {
        return _transactions.asStateFlow().map { list ->
            list.sortedByDescending { it.date }
        }
    }

    override suspend fun addTransaction(transaction: Transaction): String {
        delay(NETWORK_LATENCY_MS)
        _transactions.value += transaction
        return transaction.id
    }

    override suspend fun deleteTransaction(transactionId: String) {
        delay(NETWORK_LATENCY_MS)
        _transactions.value = _transactions.value.filterNot { it.id == transactionId }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        delay(NETWORK_LATENCY_MS)
        _transactions.value = _transactions.value.map {
            if (it.id == transaction.id) transaction else it
        }
    }

    private fun createInicialTransactions(): List<Transaction> {
        val now = LocalDateTime.now()
        return listOf(
            Transaction(
                description = "Salário Tech Group",
                amountInCents = 420_000L,
                type = TransactionType.INCOME,
                category = TransactionCategory.SALARY,
                date = now.withHour(9).withMinute(0)
            ),
            Transaction(
                description = "Supermercado",
                amountInCents = 14_520L,
                type = TransactionType.EXPENSE,
                category = TransactionCategory.FOOD,
                date = now.minusDays(1).withHour(18).withMinute(30)
            ),
            Transaction(
                description = "Aluguel mensal",
                amountInCents = 210_000L,
                type = TransactionType.EXPENSE,
                category = TransactionCategory.HOUSING,
                date = now.minusDays(16)
            ),
            Transaction(
                description = "Posto de gasolina",
                amountInCents = 14_500L,
                type = TransactionType.EXPENSE,
                category = TransactionCategory.TRANSPORTATION,
                date = now.minusDays(19)
            ),
            Transaction(
                description = "Venda de notebook usado",
                amountInCents = 65_000L,
                type = TransactionType.INCOME,
                category = TransactionCategory.OTHER,
                date = now.minusDays(22)
            ),
            Transaction(
                description = "Dividendos",
                amountInCents = 150_000L,
                type = TransactionType.INCOME,
                category = TransactionCategory.INVESTMENT,
                date = now.minusDays(22)
            ),
        )
    }

    private const val NETWORK_LATENCY_MS = 300L

}