package br.unifor.fintrack.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import br.unifor.fintrack.R
import br.unifor.fintrack.domain.model.Transaction
import br.unifor.fintrack.ui.navigation.FinTrackBottomBar
import br.unifor.fintrack.ui.screens.home.components.BalanceHeader
import br.unifor.fintrack.ui.screens.home.components.TransactionItem
import br.unifor.fintrack.ui.theme.FinTrackDimens

@Composable
fun HomeScreen(
    navController: NavController,
    onNavigateToReports: () -> Unit,
    onNavigatetoLogin: () -> Unit,
    onNavigateToTransactionDetails: () -> Unit,
    viewmodel: HomeViewModel = viewModel()
){
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewmodel.uiEvent.collect { event ->
            when(event) {
                HomeUiEvent.NavigateToReports -> onNavigateToReports()
                HomeUiEvent.NavigateToLogin -> onNavigatetoLogin()
                is HomeUiEvent.NavigateToTransactionDetails -> onNavigateToTransactionDetails()
            }
        }
    }

    Scaffold(
        topBar = {
            HomeTopBar(onLogoutClicked = viewmodel::onLogoutClicked)
        },
        bottomBar = { FinTrackBottomBar(navController = navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        HomeContent(
            uiState = uiState,
            paddingValues = innerPadding,
            onTransactionClick = viewmodel::onTransactionClicked
        )
    }
}

@Composable
private fun HomeContent(
    uiState: HomeUiState,
    paddingValues: PaddingValues,
    onTransactionClick: (String) -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        when(uiState) {
            HomeUiState.Loading -> LoadingContent()
            is HomeUiState.Error -> ErrorContent(message = uiState.message)
            is HomeUiState.Success -> SuccessContent(
                state = uiState,
                onTransactionClick = onTransactionClick
            )
        }
    }
}

@Composable
private fun LoadingContent(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(FinTrackDimens.MarginMobile),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun SuccessContent(
    state: HomeUiState.Success,
    onTransactionClick: (String) -> Unit
){
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 80.dp)
    ) {
        item {
            BalanceHeader(
                totalBalance = state.totalBalance,
                percentageChange = state.percentageChange
            )
        }

        item {
            TransactionsHeader()
        }

        if (state.isEmpty) {
            item {
                EmptyTransactionsState()
            }
        } else {
            itemsIndexed(
                items = state.transactions,
                key = { _, transaction -> transaction.id }
            ) { index, transaction ->
                TransactionRow(
                    transaction = transaction,
                    showDivider = index < state.transactions.lastIndex,
                    onClick = { onTransactionClick(transaction.id) }
                )
            }
        }
    }
}

@Composable
private fun TransactionsHeader() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FinTrackDimens.MarginMobile),
        shape = MaterialTheme.shapes.large.copy(
            bottomStart = CornerSize(0.dp),
            bottomEnd = CornerSize(0.dp)
        ),
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = FinTrackDimens.MarginMobile,
                    end = FinTrackDimens.StackSm,
                    top = FinTrackDimens.StackMd,
                    bottom = FinTrackDimens.StackSm
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Movimentações Recentes",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            TextButton(onClick = { /* TODO */ }) {
                Text(
                    text = "Ver todas",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    showDivider: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = FinTrackDimens.MarginMobile),
        shape = if (!showDivider) {
            // último item: arredondar embaixo
            MaterialTheme.shapes.large.copy(
                topStart = androidx.compose.foundation.shape.CornerSize(0.dp),
                topEnd = androidx.compose.foundation.shape.CornerSize(0.dp)
            )
        } else {
            RectangleShape
        },
        color = MaterialTheme.colorScheme.surfaceContainerLowest
    ) {
        Column {
            TransactionItem(
                transaction = transaction,
                onClick = onClick
            )
            if (showDivider) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = FinTrackDimens.MarginMobile),
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyTransactionsState(){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = FinTrackDimens.StackLg * 2),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Você ainda não tem movimentações.\nToque em + para adicionar a primeira.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onLogoutClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Image(
                painter = painterResource(id = R.drawable.ic_fintrack_logo),
                contentDescription = "FinTrack",
                modifier = Modifier.size(36.dp)
            )
        },
        navigationIcon = {
            UserAvatarMenu(onLogoutClicked = onLogoutClicked)
        },
        actions = {
            IconButton(onClick = { /* TODO: notificações */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificações",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    )
}

@Composable
private fun UserAvatarMenu(
    onLogoutClicked: () -> Unit
) {
    var menuExpanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { menuExpanded = true }) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(40.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        DropdownMenu(
            expanded = menuExpanded,
            onDismissRequest = { menuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Sair") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = null
                    )
                },
                onClick = {
                    menuExpanded = false
                    onLogoutClicked()
                }
            )
        }
    }
}