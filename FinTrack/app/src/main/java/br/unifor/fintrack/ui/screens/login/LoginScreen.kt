package br.unifor.fintrack.ui.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unifor.fintrack.R
import br.unifor.fintrack.ui.components.FinTrackPrimaryButton
import br.unifor.fintrack.ui.components.FinTrackTextField
import br.unifor.fintrack.ui.theme.FinTrackDimens

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                LoginUiEvent.NavigateToHome -> onNavigateToHome()
                LoginUiEvent.NavigateToSignUp -> onNavigateToSignUp()
            }
        }
    }

    LoginContent(
        uiState = uiState,
        onEvent = viewModel::onEvent
    )
}

@Composable
fun LoginContent(
    uiState: LoginUiState,
    onEvent: (LoginFormEvent) -> Unit
){
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
            Spacer(modifier = Modifier.height(FinTrackDimens.StackLg * 5))
            Image(
                painter = painterResource(id = R.drawable.ic_fintrack_logo),
                contentDescription = "FinTrack",
                modifier = Modifier.size(120.dp)
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
                        text = "Bem-vindo de volta. Acesse sua conta",
                        textAlign = TextAlign.Center,
                        fontSize = 25.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "E-mail",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    FinTrackTextField(
                        value = uiState.email,
                        onValueChange = { onEvent(LoginFormEvent.EmailChanged(it))},
                        label = "Digite o seu e-mail",
                        leadingIcon = Icons.Default.Email,
                        keyboardType = KeyboardType.Email,
                        isError = uiState.emailError != null,
                        errorMessage = uiState.emailError,
                        enabled = !uiState.isLoading
                    )
                    Text(
                        text = "Senha",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    FinTrackTextField(
                        value = uiState.password,
                        onValueChange = { onEvent(LoginFormEvent.PasswordChanged(it)) },
                        label = "Digite sua senha",
                        leadingIcon = Icons.Default.Lock,
                        isPassword = true,
                        keyboardType = KeyboardType.Password,
                        isError = uiState.passwordError != null,
                        errorMessage = uiState.passwordError,
                        enabled = true
                    )
                    if (uiState.generalError != null) {
                        Text(
                            text = uiState.generalError,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Spacer(modifier = Modifier.height(FinTrackDimens.StackSm))
                    FinTrackPrimaryButton(
                        text = "Entrar",
                        onClick = { onEvent(LoginFormEvent.LoginClicked) },
                        enabled = uiState.isLoginButtonEnabled,
                        isLoading = uiState.isLoading
                    )
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TextButton(
                            onClick = { onEvent(LoginFormEvent.CreateAccountClicked) },
                            enabled = !uiState.isLoading
                        ) {
                            Text(
                                text = buildAnnotatedString {
                                    append("Não tem uma conta? ")
                                    withStyle(
                                        SpanStyle(
                                            color = MaterialTheme.colorScheme.primary,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    ){
                                        append("Cadastre-se")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(FinTrackDimens.StackLg))
                }
            }
        }
    }
}