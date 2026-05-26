package br.unifor.fintrack.ui.screens.signup

sealed interface SignUpFormEvent {
    data class FullNameChanged(val name: String) : SignUpFormEvent
    data class EmailChanged(val email: String) : SignUpFormEvent
    data class PasswordChanged(val password: String) : SignUpFormEvent
    data class PasswordConfirmationChanged(val confirmation: String) : SignUpFormEvent
    data class TermsAcceptedChanged(val accepted: Boolean) : SignUpFormEvent
    data object SignUpClicked : SignUpFormEvent
    data object LoginLinkClicked : SignUpFormEvent
    data object TermsLinkClicked : SignUpFormEvent
    data object PrivacyLinkClicked : SignUpFormEvent
}