package br.unifor.fintrack.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlin.reflect.KClass

data class BottomNavItem(
    val destination: FinTrackDestination,
    val destinationClass: KClass<out FinTrackDestination>,
    val label: String,
    val icon: ImageVector
)

private val bottomNavItems = listOf(
    BottomNavItem(
        destination = FinTrackDestination.Home,
        destinationClass = FinTrackDestination.Home::class,
        label =  "Início",
        icon = Icons.Default.Home
    ),
    BottomNavItem(
        destination = FinTrackDestination.AddTransaction,
        destinationClass = FinTrackDestination.AddTransaction::class,
        label =  "Adicionar",
        icon = Icons.Default.Add
    ),
    BottomNavItem(
        destination = FinTrackDestination.Reports,
        destinationClass = FinTrackDestination.Reports::class,
        label =  "Relatórios",
        icon = Icons.Default.BarChart
    ),
)

@Composable
fun FinTrackBottomBar(navController: NavController) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any {
                it.hasRoute(item.destinationClass)
            } == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    navController.navigate(item.destination) {
                        popUpTo(FinTrackDestination.Home){
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(imageVector = item.icon, contentDescription = null)
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    indicatorColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    }
}