package br.unifor.fintrack.ui.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    private val _uiEvent = Channel<SignUpUiEvent>(Channel.BUFFERED)
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpFormEvent) = when (event) {
        is SignUpFormEvent.FullNameChanged -> onFullNameChanged(event.name)
        is SignUpFormEvent.EmailChanged -> onEmailChanged(event.email)
        is SignUpFormEvent.PasswordChanged -> onPasswordChanged(event.password)
        is SignUpFormEvent.PasswordConfirmationChanged -> onPasswordConfirmationChanged(event.confirmation)
        is SignUpFormEvent.TermsAcceptedChanged -> onTermsAcceptedChanged(event.accepted)
        SignUpFormEvent.SignUpClicked -> onSignUpClicked()
        SignUpFormEvent.LoginLinkClicked -> onLoginLinkClicked()
        SignUpFormEvent.TermsLinkClicked -> onUrlClicked(TERMS_URL)
        SignUpFormEvent.PrivacyLinkClicked -> onUrlClicked(PRIVACY_URL)
    }

    private fun onFullNameChanged(name: String) {
        _uiState.update {
            it.copy(fullName = name, fullNameError = null, generalError = null)
        }
    }

    private fun onEmailChanged(email: String) {
        _uiState.update {
            it.copy(email = email, emailError = null, generalError = null)
        }
    }

    private fun onPasswordChanged(password: String) {
        _uiState.update {
            it.copy(password = password, passwordError = null, generalError = null)
        }
    }

    private fun onPasswordConfirmationChanged(confirmation: String) {
        _uiState.update {
            it.copy(
                passwordConfirmation = confirmation,
                passwordConfirmationError = null,
                generalError = null
            )
        }
    }

    private fun onTermsAcceptedChanged(accepted: Boolean) {
        _uiState.update { it.copy(termsAccepted = accepted, termsError = null) }
    }

    private fun onSignUpClicked() {
        val state = _uiState.value

        val nameResult = Validators.validateFullName(state.fullName)
        val emailResult = Validators.validateEmail(state.email)
        val passwordResult = Validators.validatePassword(state.password)
        val confirmationResult = Validators.validatePasswordConfirmation(
            password = state.password,
            confirmation = state.passwordConfirmation
        )
        val termsError = if (!state.termsAccepted) {
            "Você precisa aceitar os termos para continuar"
        } else null

        val hasErrors = nameResult is ValidationResult.Invalid ||
                emailResult is ValidationResult.Invalid ||
                passwordResult is ValidationResult.Invalid ||
                confirmationResult is ValidationResult.Invalid ||
                termsError != null

        if (hasErrors) {
            _uiState.update {
                it.copy(
                    fullNameError = (nameResult as? ValidationResult.Invalid)?.errorMessage,
                    emailError = (emailResult as? ValidationResult.Invalid)?.errorMessage,
                    passwordError = (passwordResult as? ValidationResult.Invalid)?.errorMessage,
                    passwordConfirmationError = (confirmationResult as? ValidationResult.Invalid)?.errorMessage,
                    termsError = termsError
                )
            }
            return
        }

        performSignUp()
    }

    private fun performSignUp() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, generalError = null) }
            delay(1500)
            _uiState.update { it.copy(isLoading = false) }
            _uiEvent.send(SignUpUiEvent.NavigateToHome)
        }
    }

    private fun onLoginLinkClicked() {
        viewModelScope.launch {
            _uiEvent.send(SignUpUiEvent.NavigateToLogin)
        }
    }

    private fun onUrlClicked(url: String) {
        viewModelScope.launch {
            _uiEvent.send(SignUpUiEvent.OpenUrl(url))
        }
    }

    companion object {
        private const val TERMS_URL = "https://fintrack.unifor.br/terms"
        private const val PRIVACY_URL = "https://fintrack.unifor.br/privacy"
    }
}