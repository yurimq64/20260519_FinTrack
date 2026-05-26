package br.unifor.fintrack.ui.screens.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<WelcomeUiState>(WelcomeUiState.Loading)
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    init {
        startWelcomeFlow()
    }

    private fun startWelcomeFlow() {
        viewModelScope.launch {
            val minDisplayTime = launch { delay(MIN_DISPLAY_TIME_MS) }
            val authCheck = launch { simulateAuthCheck() }

            minDisplayTime.join()
            authCheck.join()

            _uiState.value = WelcomeUiState.NavigateToLogin
        }
    }

    private suspend fun simulateAuthCheck() {
        delay(500)
    }

    companion object {
        private const val MIN_DISPLAY_TIME_MS = 1_500L
    }
}