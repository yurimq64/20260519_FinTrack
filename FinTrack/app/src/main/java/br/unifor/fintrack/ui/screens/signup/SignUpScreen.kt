package br.unifor.fintrack.ui.screens.signup

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unifor.fintrack.R
import br.unifor.fintrack.ui.components.FinTrackPrimaryButton
import br.unifor.fintrack.ui.components.FinTrackTextField
import br.unifor.fintrack.ui.theme.FinTrackDimens

@Composable
fun SignUpScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: SignUpViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                SignUpUiEvent.NavigateToHome -> onNavigateToHome()
                SignUpUiEvent.NavigateToLogin -> onNavigateToLogin()
                is SignUpUiEvent.OpenUrl -> {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.url))
                    context.startActivity(intent)
                }
            }
        }
    }

    SignUpContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun SignUpContent(
    uiState: SignUpUiState,
    onEvent: (SignUpFormEvent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = FinTrackDimens.MarginMobile),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(FinTrackDimens.StackLg * 2))

            Image(
                painter = painterResource(id = R.drawable.ic_fintrack_logo),
                contentDescription = "FinTrack",
                modifier = Modifier.size(72.dp)
            )

            Spacer(modifier = Modifier.height(FinTrackDimens.StackSm))

            Text(
                text = "FinTrack",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(FinTrackDimens.StackLg))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(FinTrackDimens.StackLg),
                    verticalArrangement = Arrangement.spacedBy(FinTrackDimens.StackMd)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Crie uma conta",
                        style = MaterialTheme.typography.headlineMedium,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "Comece a gerenciar suas finanças com precisão a partir de hoje.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(FinTrackDimens.StackSm))

                    FinTrackTextField(
                        value = uiState.fullName,
                        onValueChange = { onEvent(SignUpFormEvent.FullNameChanged(it)) },
                        label = "Nome Completo",
                        placeholder = "ex.: João Silva",
                        isError = uiState.fullNameError != null,
                        errorMessage = uiState.fullNameError,
                        enabled = !uiState.isLoading
                    )

                    FinTrackTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(SignUpFormEvent.EmailChanged(it)) },
                        label = "E-mail",
                        placeholder = "joao.silva@email.com",
                        keyboardType = KeyboardType.Email,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError,
                        enabled = !uiState.isLoading
                    )

                    FinTrackTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(SignUpFormEvent.PasswordChanged(it)) },
                        label = "Senha",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError,
                        enabled = !uiState.isLoading
                    )

                    FinTrackTextField(
                        value = uiState.passwordConfirmation,
                        onValueChange = {
                            onEvent(SignUpFormEvent.PasswordConfirmationChanged(it))
                        },
                        label = "Confirmar Senha",
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        isError = uiState.passwordConfirmationError != null,
                        errorMessage = uiState.passwordConfirmationError,
                        enabled = !uiState.isLoading
                    )

                    TermsCheckbox(
                        isChecked = uiState.termsAccepted,
                        onCheckedChange = {
                            onEvent(SignUpFormEvent.TermsAcceptedChanged(it))
                        },
                        onTermsClicked = { onEvent(SignUpFormEvent.TermsLinkClicked) },
                        onPrivacyClicked = { onEvent(SignUpFormEvent.PrivacyLinkClicked) },
                        errorMessage = uiState.termsError,
                        enabled = !uiState.isLoading
                    )

                    if (uiState.generalError != null) {
                        Text(
                            text = uiState.generalError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.height(FinTrackDimens.StackSm))

                    SignUpButton(
                        isLoading = uiState.isLoading,
                        enabled = uiState.isSignUpButtonEnabled,
                        onClick = { onEvent(SignUpFormEvent.SignUpClicked) }
                    )

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = { onEvent(SignUpFormEvent.LoginLinkClicked) },
                            enabled = !uiState.isLoading
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Já tem uma conta? ")
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ) {
                                        append("Entrar")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(FinTrackDimens.StackLg))
        }
    }
}

@Composable
private fun TermsCheckbox(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onTermsClicked: () -> Unit,
    onPrivacyClicked: () -> Unit,
    errorMessage: String?,
    enabled: Boolean
) {
    Column {
        Row(verticalAlignment = Alignment.Top) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )
            Column(modifier = Modifier.padding(top = 12.dp)) {
                Text(
                    text = buildAnnotatedString {
                        append("Eu concordo com os ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Termos de Serviço")
                        }
                        append(" e ")
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            append("Política de Privacidade")
                        }
                        append(".")
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row {
                    TextButton(
                        onClick = onTermsClicked,
                        enabled = enabled,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 0.dp,
                            vertical = 0.dp
                        )
                    ) {
                        Text(
                            text = "Termos",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                    TextButton(
                        onClick = onPrivacyClicked,
                        enabled = enabled,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            horizontal = 0.dp,
                            vertical = 0.dp
                        )
                    ) {
                        Text(
                            text = "Privacidade",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

@Composable
private fun SignUpButton(
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit
) {
    if (isLoading) {
        FinTrackPrimaryButton(
            text = "Cadastrar",
            onClick = onClick,
            enabled = enabled,
            isLoading = true
        )
    } else {
        androidx.compose.material3.Button(
            onClick = onClick,
            enabled = enabled,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(
                text = "Cadastrar",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null
            )
        }
    }
}