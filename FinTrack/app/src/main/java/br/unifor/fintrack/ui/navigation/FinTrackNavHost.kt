package br.unifor.fintrack.ui.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.unifor.fintrack.ui.screens.home.HomeScreen
import br.unifor.fintrack.ui.screens.login.LoginScreen
import br.unifor.fintrack.ui.screens.signup.SignUpScreen
import br.unifor.fintrack.ui.screens.transaction.AddTransactionScreen
import br.unifor.fintrack.ui.screens.welcome.WelcomeScreen

@Composable
fun FinTrackNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = FinTrackDestination.Welcome
    ) {
        composable<FinTrackDestination.Welcome> {
            WelcomeScreen(
                onNavigateToLogin = {
                    navController.navigate(FinTrackDestination.Login) {
                        popUpTo(FinTrackDestination.Welcome) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(FinTrackDestination.Home) {
                        popUpTo(FinTrackDestination.Welcome) { inclusive = true }
                    }
                }
            )
        }

        composable<FinTrackDestination.Login> {
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(FinTrackDestination.Home) {
                        popUpTo(FinTrackDestination.Login) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(FinTrackDestination.SignUp) {
                    }
                }
            )
        }

        composable<FinTrackDestination.SignUp> {
            SignUpScreen(
                onNavigateToHome = {
                    navController.navigate(FinTrackDestination.Home) {
                        popUpTo(FinTrackDestination.Login) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable<FinTrackDestination.Home> {
            HomeScreen(
                navController = navController,
                onNavigateToReports = {
                    navController.navigate(FinTrackDestination.Reports)
                },
                onNavigateToTransactionDetails = {
                    // TODO: criar tela de detalhe na próxima etapa
                },
                onNavigatetoLogin = {
                    navController.navigate(FinTrackDestination.Login) {
                        popUpTo(FinTrackDestination.Home) { inclusive = true }
                    }
                }
            )
        }

        composable<FinTrackDestination.AddTransaction> {
            AddTransactionScreen(
                onNavigateBack = { navController.popBackStack() },
                onTransactionSaved = { navController.popBackStack() }
            )
        }

        composable<FinTrackDestination.Reports> {
            PlaceholderScreen(name = "Relatórios")
        }
    }
}

@Composable
fun PlaceholderScreen(name: String) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("$name screen - em construção")
            }

        }
    }
}