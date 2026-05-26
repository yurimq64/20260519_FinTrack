package br.unifor.fintrack.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.unifor.fintrack.data.repository.FirebaseAuthRepository
import br.unifor.fintrack.domain.repository.AuthRepository
import br.unifor.fintrack.util.ValidationResult
import br.unifor.fintrack.util.Validators
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {

    private val authRepository: AuthRepository = FirebaseAuthRepository()

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<LoginUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: LoginFormEvent) {
        when(event){
            is LoginFormEvent.EmailChanged -> onEmailChanged(event.email)
            is LoginFormEvent.PasswordChanged -> onPasswordChanged(event.password)
            is LoginFormEvent.LoginClicked -> onLoginClicked()
            is LoginFormEvent.CreateAccountClicked -> onCreatedAccountClicked()
        }
    }

    private fun onEmailChanged(email:String) {
        _uiState.update {
            it.copy(
                email = email,
                emailError =  null,
                generalError = null
            )
        }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                passwordError = null,
                generalError = null
            )
        }
    }

    private fun onLoginClicked() {
        val emailResult = Validators.validateEmail(_uiState.value.email)
        val passwordResult = Validators.validatePassword(_uiState.value.password)

        if(emailResult is ValidationResult.Invalid || passwordResult is ValidationResult.Invalid) {
            _uiState.update {
                it.copy(
                    emailError = (emailResult as? ValidationResult.Invalid)?.errorMessage,
                    passwordError = (passwordResult as? ValidationResult.Invalid)?.errorMessage
                )
            }
            return
        }

        performLogin()
    }

    private fun performLogin() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            delay(1500)

            val email = _uiState.value.email
            val password = _uiState.value.password

            try {
                authRepository.signIn(email, password)
                _uiState.update { it.copy(isLoading = false) }
                _uiEvent.send(LoginUiEvent.NavigateToHome)
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        generalError = e.message
                    )
                }
            }

        }
    }

    private fun onCreatedAccountClicked() {
        viewModelScope.launch {
            _uiEvent.send(LoginUiEvent.NavigateToSignUp)
        }
    }

    companion object {
        private const val FAKE_PASSWORD = "123456"
    }
}