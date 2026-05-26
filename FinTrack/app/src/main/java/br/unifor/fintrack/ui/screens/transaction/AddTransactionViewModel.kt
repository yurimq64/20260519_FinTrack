package br.unifor.fintrack.ui.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.unifor.fintrack.data.repository.FakeTransactionRepository
import br.unifor.fintrack.domain.model.Transaction
import br.unifor.fintrack.domain.model.TransactionCategory
import br.unifor.fintrack.domain.model.TransactionType
import br.unifor.fintrack.domain.repository.TransactionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class AddTransactionViewModel(
    private val repository: TransactionRepository = FakeTransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<AddTransactionUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddTransactionFormEvent) {
        when (event) {
            is AddTransactionFormEvent.TypeChanged -> onTypeChanged(event.type)
            is AddTransactionFormEvent.AmountDigitPressed -> onDigitPressed(event.digit)
            AddTransactionFormEvent.AmountBackspacePressed -> onBackspace()
            is AddTransactionFormEvent.CategoryChanged -> onCategoryChanged(event.category)
            is AddTransactionFormEvent.DateChanged -> onDateChanged(event.date)
            is AddTransactionFormEvent.NoteChanged -> onNoteChanged(event.note)
            AddTransactionFormEvent.SaveClicked -> onSave()
            AddTransactionFormEvent.BackClicked -> onBack()
        }
    }

    private fun onTypeChanged(type: TransactionType) {
        _uiState.update {
            it.copy(
                type = type,
                category = null,
                categoryError = null
            )
        }
    }

    private fun onDigitPressed(digit: Int) {
        val current = _uiState.value.amountInCents
        val newValue = (current * 10) + digit
        if (newValue > MAX_AMOUNT_IN_CENTS) return
        _uiState.update {
            it.copy(amountInCents = newValue, amountError = null)
        }
    }

    private fun onBackspace() {
        _uiState.update {
            it.copy(amountInCents = it.amountInCents / 10, amountError = null)
        }
    }

    private fun onCategoryChanged(category: TransactionCategory) {
        _uiState.update { it.copy(category = category, categoryError = null) }
    }

    private fun onDateChanged(date: LocalDate) {
        _uiState.update { it.copy(date = date) }
    }

    private fun onNoteChanged(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    private fun onSave() {
        val state = _uiState.value

        val amountError = if(state.amountInCents <= 0) "Informe um valor" else null
        val categoryError = if(state.category == null) "Selecione uma categoria" else null

        if(amountError != null || categoryError != null) {
            _uiState.update {
                it.copy(amountError = amountError, categoryError = categoryError)
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, generalError = null) }

            try {
                val transaction = Transaction(
                    description = state.category!!.displayName,
                    amountInCents = state.amountInCents,
                    type = state.type,
                    category = state.category,
                    date = LocalDateTime.of(state.date, LocalTime.now()),
                    note = state.note.takeIf { it.isNotBlank() }
                )
                repository.addTransaction(transaction)
                _uiEvent.send(AddTransactionUiEvent.TransactionSaved)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        generalError = e.message ?: "Erro ao salvar movimentação"
                    )
                }
            }
        }
    }

    private fun onBack() {
        viewModelScope.launch {
            _uiEvent.send(AddTransactionUiEvent.NavigateBack)
        }
    }


    companion object {
        private const val MAX_AMOUNT_IN_CENTS = 9_999_999_999L
    }
}