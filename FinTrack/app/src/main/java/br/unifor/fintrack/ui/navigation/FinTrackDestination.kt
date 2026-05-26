package br.unifor.fintrack.ui.navigation

import kotlinx.serialization.Serializable

sealed interface FinTrackDestination {
    @Serializable
    data object Welcome: FinTrackDestination

    @Serializable
    data object Login: FinTrackDestination

    @Serializable
    data object SignUp: FinTrackDestination

    @Serializable
    data object Home: FinTrackDestination

    @Serializable
    data object AddTransaction: FinTrackDestination

    @Serializable
    data object Reports: FinTrackDestination
}