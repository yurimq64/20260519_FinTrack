package br.unifor.fintrack.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.unifor.fintrack.data.repository.FakeTransactionRepository
import br.unifor.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: TransactionRepository = FakeTransactionRepository
): ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<HomeUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        observeTransactions()
    }

    private fun observeTransactions() {
        viewModelScope.launch {
            repository.observeTransactions()
                .catch { error ->
                    _uiState.value = HomeUiState.Error(
                        message = error.message ?: "Erro ao carregar transações"
                    )
                }
                .collect { transactions ->
                    val totalBalance = transactions.sumOf { it.signedAmountInCents }  // ← Long agora
                    _uiState.value = HomeUiState.Success(
                        transactions = transactions,
                        totalBalance = totalBalance,
                        percentageChange = 2.4
                    )
                }
        }
    }


    fun onReportsClicked() {
        viewModelScope.launch {
            _uiEvent.send(HomeUiEvent.NavigateToReports)
        }
    }

    fun onTransactionClicked(transactionId: String) {
        viewModelScope.launch {
            _uiEvent.send(HomeUiEvent.NavigateToTransactionDetails(transactionId))
        }
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            _uiEvent.send(HomeUiEvent.NavigateToLogin)
        }
    }
}