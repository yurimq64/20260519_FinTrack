package br.unifor.fintrack.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.unifor.fintrack.domain.model.Transaction
import br.unifor.fintrack.domain.model.TransactionCategory
import br.unifor.fintrack.domain.model.TransactionType
import br.unifor.fintrack.ui.theme.FinTrackDimens
import br.unifor.fintrack.util.CurrencyFormatter
import br.unifor.fintrack.util.DateFormatter

@Composable
fun TransactionItem(
    transaction: Transaction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = FinTrackDimens.MarginMobile,
                vertical = FinTrackDimens.StackMd
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(FinTrackDimens.StackMd)
    ) {
        CategoryIcon(category = transaction.category, type = transaction.type)

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Text(
                text = DateFormatter.formatRelative(transaction.date),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Text(
            text = CurrencyFormatter.formatCentsWithSign(
                amountInCents = transaction.amountInCents,
                isIncome = transaction.type == TransactionType.INCOME
            ),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = if (transaction.type == TransactionType.INCOME) {
                MaterialTheme.colorScheme.secondary
            } else {
                MaterialTheme.colorScheme.tertiary
            }
        )
    }
}

@Composable
private fun CategoryIcon(
    category: TransactionCategory,
    type: TransactionType
) {
    Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.size(48.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Icon(
                imageVector = iconForCategory(category),
                contentDescription = category.displayName,
                tint = if (type == TransactionType.INCOME) {
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

private fun iconForCategory(category: TransactionCategory): ImageVector {
    return when(category) {
        TransactionCategory.HOUSING -> Icons.Default.Home
        TransactionCategory.FOOD -> Icons.Default.Fastfood
        TransactionCategory.TRANSPORTATION -> Icons.Default.DirectionsCar
        TransactionCategory.ENTERTAINMENT -> Icons.Default.SportsEsports
        TransactionCategory.HEALTH -> Icons.Default.LocalHospital
        TransactionCategory.EDUCATION -> Icons.Default.School
        TransactionCategory.SHOPPING -> Icons.Default.ShoppingCart
        TransactionCategory.SALARY -> Icons.Default.Work
        TransactionCategory.FREELANCE -> Icons.Default.AttachMoney
        TransactionCategory.INVESTMENT -> Icons.AutoMirrored.Filled.TrendingUp
        TransactionCategory.OTHER -> Icons.Default.Sell
    }
}