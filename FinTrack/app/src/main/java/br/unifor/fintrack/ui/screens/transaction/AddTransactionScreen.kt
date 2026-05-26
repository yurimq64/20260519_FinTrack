package br.unifor.fintrack.ui.screens.transaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import br.unifor.fintrack.ui.components.FinTrackPrimaryButton
import br.unifor.fintrack.ui.screens.transaction.components.AmountInput
import br.unifor.fintrack.ui.screens.transaction.components.CategoryDropdown
import br.unifor.fintrack.ui.screens.transaction.components.TransactionTypeToggle
import br.unifor.fintrack.ui.screens.addtransaction.components.DatePickerField
import br.unifor.fintrack.ui.theme.FinTrackDimens


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onNavigateBack: () -> Unit,
    onTransactionSaved: () -> Unit,
    viewModel: AddTransactionViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                AddTransactionUiEvent.NavigateBack -> onNavigateBack()
                AddTransactionUiEvent.TransactionSaved -> onTransactionSaved()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nova Movimentação",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.onEvent(AddTransactionFormEvent.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        AddTransactionContent(
            uiState = uiState,
            paddingValues = innerPadding,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun AddTransactionContent(
    uiState: AddTransactionUiState,
    paddingValues: PaddingValues,
    onEvent: (AddTransactionFormEvent) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = FinTrackDimens.MarginMobile),
        verticalArrangement = Arrangement.spacedBy(FinTrackDimens.StackLg)
    ) {
        Spacer(Modifier.height(FinTrackDimens.StackSm))

        TransactionTypeToggle(
            selectedType = uiState.type,
            onTypeSelected = { onEvent(AddTransactionFormEvent.TypeChanged(it)) }
        )

        AmountInput(
            amountInCents = uiState.amountInCents,
            hasError = uiState.amountError != null,
            errorMessage = uiState.amountError
        )

        NumericKeypad(
            onDigitPressed = { onEvent(AddTransactionFormEvent.AmountDigitPressed(it)) },
            onBackspace = { onEvent(AddTransactionFormEvent.AmountBackspacePressed) }
        )

        Surface(
            shape = MaterialTheme.shapes.large,
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(FinTrackDimens.StackMd),
                verticalArrangement = Arrangement.spacedBy(FinTrackDimens.StackMd)
            ) {
                Text(
                    text = "Categoria",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                CategoryDropdown(
                    selectedCategory = uiState.category,
                    availableCategories = uiState.availableCategories,
                    onCategorySelected = { onEvent(AddTransactionFormEvent.CategoryChanged(it)) },
                    isError = uiState.categoryError != null,
                    errorMessage = uiState.categoryError
                )

                Text(
                    text = "Data",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                DatePickerField(
                    selectedDate = uiState.date,
                    onDateSelected = { onEvent(AddTransactionFormEvent.DateChanged(it)) }
                )

                Text(
                    text = "Observação",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                OutlinedTextField(
                    value = uiState.note,
                    onValueChange = { onEvent(AddTransactionFormEvent.NoteChanged(it)) },
                    placeholder = { Text("Para que foi essa movimentação?") },
                    shape = MaterialTheme.shapes.small,
                    minLines = 2,
                    maxLines = 4,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (uiState.generalError != null) {
            Text(
                text = uiState.generalError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        FinTrackPrimaryButton(
            text = "Salvar Movimentação",
            onClick = { onEvent(AddTransactionFormEvent.SaveClicked) },
            enabled = uiState.isSaveEnabled,
            isLoading = uiState.isSaving
        )

        Spacer(Modifier.height(FinTrackDimens.StackLg))
    }
}

@Composable
private fun NumericKeypad(
    onDigitPressed: (Int) -> Unit,
    onBackspace: () -> Unit
) {
    val rows = listOf(
        listOf("1", "2", "3"),
        listOf("4", "5", "6"),
        listOf("7", "8", "9"),
        listOf("00", "0", "<")
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(FinTrackDimens.StackSm)
    ) {
        rows.forEach { row ->
            androidx.compose.foundation.layout.Row(
                horizontalArrangement = Arrangement.spacedBy(FinTrackDimens.StackSm),
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { key ->
                    KeypadButton(
                        label = key,
                        onClick = {
                            when (key) {
                                "<" -> onBackspace()
                                "00" -> {
                                    onDigitPressed(0)
                                    onDigitPressed(0)
                                }
                                else -> onDigitPressed(key.toInt())
                            }
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
        shape = MaterialTheme.shapes.medium,
        contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
    ) {
        if (label == "<") {
            Text(
                text = "⌫",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        } else {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}